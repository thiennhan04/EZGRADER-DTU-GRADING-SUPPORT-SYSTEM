import cv2, imutils, numpy as np
from imutils import contours
from imutils.perspective import four_point_transform

def show(name,imagetoshow,standard=500):
	h,w = imagetoshow.shape[0:2]
	rate = -1
	if h>=w:
		w=standard/h*w
		h=standard
	else:
		h=standard/w*h
		w=standard
	imagetoshow = cv2.resize(imagetoshow,(int(w),int(h)))
	cv2.imshow(str(name),imagetoshow)

def getCnt(img,minVal=-1,maxVal=-1):
	imggray =  cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
	blurred = cv2.GaussianBlur(imggray, (3, 3), 0)
	edged = cv2.Canny(blurred, 50, 200)
	cnts = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	cnts = imutils.grab_contours(cnts)
	neoCnt = []
	cnts = sorted(cnts, key=cv2.contourArea, reverse=True)
	for c in cnts:
		peri = cv2.arcLength(c, True)
		approx = cv2.approxPolyDP(c, 0.02 * peri, True)
		area = cv2.contourArea(approx)
		if len(approx) == 4 and ((minVal<=area and area<=maxVal and minVal!=-1 and maxVal!=-1)
			or (minVal<=area and minVal!=-1 and maxVal==-1)
			or (area<=maxVal and minVal==-1 and maxVal!=-1)
			or (minVal==-1 and maxVal==-1)):
			neoCnt.append(approx)
	return neoCnt

def transform_img_input(img):
	listCnt = np.array(getCnt(img.copy(),1200,2000))
	shapecvt = np.array([[0,0]]*4)

	cntleft = np.array(  contours.sort_contours(listCnt, method="left-to-right")[0][0:3]  )
	cntleft = np.array(  contours.sort_contours(cntleft, method="top-to-bottom")[0]  )

	cntright = np.array(  contours.sort_contours(listCnt, method="right-to-left")[0][0:3]  )
	cntright = np.array(  contours.sort_contours(cntright, method="top-to-bottom")[0]  )
	
	shapecvt[0] = cntleft[0][0] + (-10,-10)
	shapecvt[1] = cntleft[2][1] + (-10, 10)
	shapecvt[2] = cntright[2][2] + (10, 10)
	shapecvt[3] = cntright[0][3] + (10,-10)

	imgcopy = four_point_transform(img.copy(), shapecvt.reshape(4, 2))
	return imgcopy

def cropimg(img,point_start,point_end):
	x1, y1 = point_start[0], point_start[1]
	x2, y2 = point_end[0], point_end[1]
	x, y = min(x1, x2), min(y1, y2)
	w, h = abs(x2-x1), abs(y2-y1)
	cropped = img[y:y+h, x:x+w]
	return cropped

def crop_1(img,listCnt):
	shapecvt = np.array([[0,0],[0,0]])
	cntsbd = np.array(contours.sort_contours(listCnt, method="left-to-right")[0][3:8])
	cntsbd1 = np.array(contours.sort_contours(cntsbd[0:2], method="top-to-bottom")[0])
	shapecvt[0] = cntsbd1[0][2]
	shapecvt[1] = cntsbd1[1][3]

	cntsbd2 = np.array(contours.sort_contours(cntsbd[2:5], method="top-to-bottom")[0])
	shapecvt[1] = cntsbd2[1][0]

	imgcopy = cropimg(img,shapecvt[0],shapecvt[1])
	return imgcopy

def crop_2(img,listCnt):
	cntans = np.array(contours.sort_contours(listCnt, method="left-to-right")[0])
	cntans[1:7] = np.array(contours.sort_contours(cntans[1:7], method="top-to-bottom")[0])
	cntans[7:13] = np.array(contours.sort_contours(cntans[7:13], method="top-to-bottom")[0])
	listleft = [np.round(cv2.mean(cntans[0])[0:2]).astype("int")]
	listright = []
	for i in range(2,7):
		listright.append(np.round(cv2.mean(cntans[i])[0:2]).astype("int"))
		if i!=6:
			listleft.append([listleft[i-2][0],listright[i-2][1]])
	point = np.round(cv2.mean(cntans[13])[0:2]).astype("int")
	for i in range(7,12):
		listleft.append(np.round(cv2.mean(cntans[i])[0:2]).astype("int"))
		p1 = [point[0],np.round(cv2.mean(cntans[i+1])[0:2]).astype("int")[1]]
		listright.append(p1)

	listleft = np.array(listleft)
	listright = np.array(listright)
	listimg = []
	for i in range(0,10):
		imgcopy = cropimg(img, listleft[i], listright[i])
		listimg.append(imgcopy)
	return listimg

def findAns(img,listCircle,num,isVertical):
	if isVertical==True:
		listCircle = np.array(sorted(listCircle, key=lambda k: k[0]))
	else:
		listCircle = np.array(sorted(listCircle, key=lambda k: k[1]))
	thresh = cv2.threshold(cv2.cvtColor(img.copy(), cv2.COLOR_BGR2GRAY), 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
	anschoose = []
	for (col, index) in enumerate(np.arange(0, len(listCircle), num)):
		cnts = listCircle[index:index+num]
		if isVertical==True:
			cnts = np.array(sorted(cnts, key=lambda k: k[1]))
		else:
			cnts = np.array(sorted(cnts, key=lambda k: k[0]))

		bubbled = []
		for (j,(x,y,_)) in enumerate(cnts):
			mask = np.zeros(thresh.shape, dtype="uint8").copy()
			cv2.circle(mask, (x, y), 10, 255, -1)
			mask = cv2.bitwise_and(thresh, thresh, mask=mask)
			total = cv2.countNonZero(mask)
			if total > 300:
				bubbled.append(j)
		color = (0,255,0)
		if len(bubbled)!=1:
			color = (0,0,255)  # (b,g,r)  (r,g,b)
			anschoose.append('#')
		else:
			anschoose.append(bubbled[0])
		for j in bubbled:
			x,y,_ = cnts[j]
			cv2.circle(img, (x, y), 14, color, 3)
	return ''.join(map(str,anschoose))

def get_Sbd_and_maDe(img):
	neoCnt = getCnt(img.copy(),200,500)
	neoCnt = np.array(contours.sort_contours(neoCnt, method="top-to-bottom")[0])
	point0 = np.round(cv2.mean(neoCnt[0])[0:2]).astype("int")
	point2 = np.round(cv2.mean(neoCnt[2])[0:2]).astype("int")
	w = img.shape[1]
	img1 = cropimg(img,(0,point0[1]),(point2[0],point2[1]))
	img2 = cropimg(img,(point2[0],point0[1]),(w,point2[1]))

	circles1 = detect_circles_and_get_ans(img1)
	circles2 = detect_circles_and_get_ans(img2)

	sbd = findAns(img1,circles1,10,True)
	made = findAns(img2,circles2,10,True)
	return sbd,made

def get_ans(img):
	circles = detect_circles_and_get_ans(img)
	ans = findAns(img,circles,4,False)
	return ans

def detect_circles_and_get_ans(img):
	imggray =  cv2.cvtColor(img.copy(), cv2.COLOR_BGR2GRAY)
	blurred = cv2.GaussianBlur(imggray, (3, 3), 0)
	circles = cv2.HoughCircles(blurred, cv2.HOUGH_GRADIENT, 1, 20, param1=40, param2=20, minRadius=9, maxRadius=15)[0]
	if circles is not None:
		circles = np.round(circles[::]).astype("int")
	return circles

def run():
	imgpath = 'images/test21-choose.png'
	img = cv2.imread(imgpath).copy()
	img = cv2.resize(img,(1654,2339))

	img = transform_img_input(img)
	img = cv2.resize(img,(1466,2136))

	neoBigCnt = getCnt(img,1200,2000)
	neoSmallCnt = getCnt(img,500,1200)
	neoBigCnt = np.array(neoBigCnt)
	neoSmallCnt = np.array(neoSmallCnt)

	img_sbd_and_made = crop_1(img,neoBigCnt)
	sbd, maDe = get_Sbd_and_maDe(img_sbd_and_made)

	# print('sbd: '+sbd)
	# print('made: '+maDe)
	if '#' not in maDe:
		listimgans = crop_2(img,neoSmallCnt)
		ans = []
		for i in range(len(listimgans)):
			ans.append(get_ans(listimgans[i]))
			show('img'+str(i+100),listimgans[i])
		# print('ans: '+str(ans))
	show('img',img,1000)
	show('img_sbd_and_made',img_sbd_and_made,500)
	cv2.waitKey(0)

########## end ##########

run()
