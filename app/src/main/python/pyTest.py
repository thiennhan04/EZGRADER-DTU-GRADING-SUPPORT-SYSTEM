import numpy as np
import cv2
from PIL import Image
from keras.preprocessing import image
import tensorflow as tf
import pathlib, os, sys
import imutils
from imutils.perspective import four_point_transform
from imutils import contours

# đường dẫn ảnh
imgpath = 'images/test1.png'
# file ảnh cần quét
img = cv2.imread(imgpath).copy()
img = cv2.resize(img,(1654,2339))

def findSBD():
	image =  cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
	blurred = cv2.GaussianBlur(image, (5, 5), 0)
	edged = cv2.Canny(blurred, 50, 200)
	cnts = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	cnts = imutils.grab_contours(cnts)
	docCnt = None

	if len(cnts) > 0:
		cnts = sorted(cnts, key=cv2.contourArea, reverse=True)
		for c in cnts:
			peri = cv2.arcLength(c, True)
			approx = cv2.approxPolyDP(c, 0.02 * peri, True)	
			if len(approx) == 4 and cv2.contourArea(approx)>225:
				docCnt = approx
				break
		# for c in cnts:
		# 	peri = cv2.arcLength(c, True)
		# 	approx = cv2.approxPolyDP(c, 0.02 * peri, True)	
		# 	if len(approx) == 4 and cv2.contourArea(approx)>225:
		# 		cv2.drawContours(img, [c], -1, (0,255,0), 3)
		# cv2.imwrite('img//drawedgefilter.png',img)
		# return
	# cv2.drawContours(img, cnts, -1, (0,255,0), 3)
	cv2.imshow("img",cv2.resize(img,(int(img.shape[1]/2.5),int(img.shape[0]/2.5))))

	paper = four_point_transform(img, docCnt.reshape(4, 2))

	warped = four_point_transform(image, docCnt.reshape(4, 2))

	thresh = cv2.threshold(warped, 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]

	cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	cnts = imutils.grab_contours(cnts)

	# cv2.drawContours(paper, cnts, -1, (0,255,0), 3)

	questionCnts = []
	for c in cnts:
		(x, y, w, h) = cv2.boundingRect(c)
		ar = w / float(h)
		if w >= 15 and h >= 15 and ar >= 0.8 and ar <= 1.2:
			questionCnts.append(c)

	questionCnts = contours.sort_contours(questionCnts, method="left-to-right")[0]

	check = True
	sbd = []
	for (q, i) in enumerate(np.arange(0, len(questionCnts), 10)):
		cnts = contours.sort_contours(questionCnts[i:i + 10], method="top-to-bottom")[0]

		bubbled = []
		for (j, c) in enumerate(cnts):
			mask = np.zeros(thresh.shape, dtype="uint8").copy()
			cv2.drawContours(mask, [c], -1, 255, -1)
			mask = cv2.bitwise_and(thresh, thresh, mask=mask)
			total = cv2.countNonZero(mask)
			if total > 1200:
				bubbled.append(j)

		color = (0,255,0)
		if len(bubbled)>1:
			color = (0,0,255)
			check = False
		for choose in bubbled:
			cv2.drawContours(paper, [cnts[choose]], -1, color, 3)
			sbd.append(choose)

	# cv2.imwrite('img//choose1.png',paper)
	# cv2.imshow("SBD2",cv2.resize(paper,(int(paper.shape[1]),int(paper.shape[0]))))
	if check == True:
		return (check,''.join(map(str, sbd)))
	else:
		return (check,paper)

def findMaDe():
	image =  cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
	blurred = cv2.GaussianBlur(image, (5, 5), 0)
	edged = cv2.Canny(blurred, 75, 200)
	cnts = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	cnts = imutils.grab_contours(cnts)
	docCnt = None

	if len(cnts) > 0:
		cnts = sorted(cnts, key=cv2.contourArea, reverse=True)
		for i in range(0,len(cnts)):
			peri = cv2.arcLength(cnts[i], True)
			approx = cv2.approxPolyDP(cnts[i], 0.02 * peri, True)
			if len(approx) == 4 and cv2.contourArea(approx)>225:
				docCnt = np.array(sorted(approx , key=lambda k: [k[0][1], k[0][0]]))
			if i==2:
				break
	paper = four_point_transform(img, docCnt.reshape(4, 2))
	warped = four_point_transform(image, docCnt.reshape(4, 2))
	warped = warped[0:int(warped.shape[0]),0:int(warped.shape[1]*3/4)]
	thresh = cv2.threshold(warped, 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
	
	cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	cnts = imutils.grab_contours(cnts)

	questionCnts = []
	for c in cnts:
		(x, y, w, h) = cv2.boundingRect(c)
		ar = w / float(h)
		if w >= 15 and h >= 15 and ar >= 0.8 and ar <= 1.2:
			questionCnts.append(c)

	questionCnts = contours.sort_contours(questionCnts, method="left-to-right")[0]

	check = True
	sbd = []
	for (q, i) in enumerate(np.arange(0, len(questionCnts), 10)):
		cnts = contours.sort_contours(questionCnts[i:i + 10], method="top-to-bottom")[0]

		bubbled = []
		for (j, c) in enumerate(cnts):
			mask = np.zeros(thresh.shape, dtype="uint8").copy()
			cv2.drawContours(mask, [c], -1, 255, -1)
			mask = cv2.bitwise_and(thresh, thresh, mask=mask)
			total = cv2.countNonZero(mask)
			if total > 1100:
				bubbled.append(j)

		color = (0,255,0)
		if len(bubbled)>1:
			color = (0,0,255)
			check = False
		for choose in bubbled:
			cv2.drawContours(paper, [cnts[choose]], -1, color, 3)
			sbd.append(choose)

	# cv2.imshow("MaDe",cv2.resize(paper,(int(paper.shape[1]),int(paper.shape[0]))))
	if check==True:
		return (check,''.join(map(str,sbd)))
	else:
		return (check,paper)

def findans(docCnt):
	image =  cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
	paper = four_point_transform(img, docCnt.reshape(4, 2))
	warped = four_point_transform(image, docCnt.reshape(4, 2))
	thresh = cv2.threshold(warped, 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
	
	cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	cnts = imutils.grab_contours(cnts)

	questionCnts = []
	for c in cnts:
		(x, y, w, h) = cv2.boundingRect(c)
		ar = w / float(h)
		if w >= 15 and h >= 15 and ar >= 0.8 and ar <= 1.2:
			questionCnts.append(c)
	# cv2.drawContours(paper, questionCnts, -1, (0,255,0), 3)
	# cv2.imshow("paper"+str(docCnt),cv2.resize(paper,(int(paper.shape[1]),int(paper.shape[0]))))
	# return

	questionCnts = contours.sort_contours(questionCnts, method="top-to-bottom")[0]

	check = True
	sbd = []
	for (q, i) in enumerate(np.arange(0, len(questionCnts), 4)):
		cnts = contours.sort_contours(questionCnts[i:i + 4])[0]

		bubbled = []
		for (j, c) in enumerate(cnts):
			mask = np.zeros(thresh.shape, dtype="uint8").copy()
			cv2.drawContours(mask, [c], -1, 255, -1)
			mask = cv2.bitwise_and(thresh, thresh, mask=mask)
			total = cv2.countNonZero(mask)
			if total > 1100:
				bubbled.append(j)

		color = (0,255,0)
		if len(bubbled)==0 or len(bubbled)>1:
			color = (0,255,255)
			check = False
			sbd.append('E')
		else:
			if bubbled[0]==0:
				sbd.append('A')
			elif bubbled[0]==1:
				sbd.append('B')
			elif bubbled[0]==2:
				sbd.append('C')
			elif bubbled[0]==3:
				sbd.append('D')
		for choose in bubbled:
			cv2.drawContours(paper, [cnts[choose]], -1, color, 3)
			cv2.putText(paper, "{}".format(sbd[len(sbd)-1]), (cnts[choose][0][0][0], cnts[choose][0][0][1]),
								cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 0, 255), 2)
	# cv2.imwrite('img//annswer2.png',paper)

	cv2.imshow("answer"+str(docCnt),cv2.resize(paper,(int(paper.shape[1]),int(paper.shape[0]))))
	if check==True:
		return (check,''.join(map(str,sbd)))
	else:
		return (check,''.join(map(str,sbd)))

def findAnswer():
	image =  cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
	blurred = cv2.GaussianBlur(image, (5, 5), 0)
	edged = cv2.Canny(blurred, 75, 200)
	cnts = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	cnts = imutils.grab_contours(cnts)
	imgshow = img.copy()
	docCnt1 = None
	docCnt2 = None
	docCnt3 = None

	if len(cnts) > 0:
		# cnts = sorted(cnts, key=cv2.contourArea, reverse=True)
		cnts = contours.sort_contours(cnts, method="bottom-to-top")[0]
		for i in range(0,len(cnts)):
			peri = cv2.arcLength(cnts[i], True)
			approx = cv2.approxPolyDP(cnts[i], 0.02 * peri, True)
			if len(approx) == 4 and cv2.contourArea(approx)>800 and cv2.contourArea(approx)<1100:
				docCnt1 = np.array(sorted(approx , key=lambda k: [k[0][1], k[0][0]]))
				break
		cnts = contours.sort_contours(cnts, method="left-to-right")[0]
		for i in range(0,len(cnts)):
			peri = cv2.arcLength(cnts[i], True)
			approx = cv2.approxPolyDP(cnts[i], 0.02 * peri, True)
			if len(approx) == 4 and cv2.contourArea(approx)>800 and cv2.contourArea(approx)<1100:
				docCnt2 = np.array(sorted(approx , key=lambda k: [k[0][1], k[0][0]]))
				break
		cnts = contours.sort_contours(cnts, method="right-to-left")[0]
		for i in range(0,len(cnts)):
			peri = cv2.arcLength(cnts[i], True)
			approx = cv2.approxPolyDP(cnts[i], 0.02 * peri, True)
			if len(approx) == 4 and cv2.contourArea(approx)>800 and cv2.contourArea(approx)<1100:
				docCnt3 = np.array(sorted(approx , key=lambda k: [k[0][1], k[0][0]]))
				break

	docCntTN1 = docCnt2.copy()
	docCntTN1[0][0][0] = docCnt2[2][0][0]+5
	docCntTN1[0][0][1] = docCnt2[2][0][1]+5
	docCntTN1[1][0][0] = int(docCnt1[0][0][0]*0.96)
	docCntTN1[1][0][1] = docCnt2[2][0][1]+5
	docCntTN1[2][0][0] = int(docCnt1[0][0][0]*0.96)
	docCntTN1[2][0][1] = docCnt1[0][0][1]-5
	docCntTN1[3][0][0] = docCnt2[2][0][0]+5
	docCntTN1[3][0][1] = docCnt1[0][0][1]-5


	docCntTN2 = docCnt3.copy()
	docCntTN2[0][0][0] = docCnt1[1][0][0]+10
	docCntTN2[0][0][1] = docCnt3[3][0][1]+5
	docCntTN2[1][0][0] = int(docCnt3[3][0][0]*0.96)
	docCntTN2[1][0][1] = docCnt3[3][0][1]+5
	docCntTN2[2][0][0] = int(docCnt3[3][0][0]*0.96)
	docCntTN2[2][0][1] = docCnt1[1][0][1]-5
	docCntTN2[3][0][0] = docCnt1[1][0][0]+10
	docCntTN2[3][0][1] = docCnt1[1][0][1]-5

	# cv2.drawContours(img, [docCntTN2], -1, (0,255,0), 3)
	# cv2.imshow("img",cv2.resize(img,(int(img.shape[1]/2.5),int(img.shape[0]/2.5))))
	# return (None,'')
	ans1 = findans(docCntTN1)
	ans2 = findans(docCntTN2)
	return (ans1[0] and ans2[0],ans1[1]+ans2[1])

def ex1():
	# lấy số báo danh
	getSBD = findSBD()
	sobaodanh = -1
	if getSBD[0] == True:
		sobaodanh = getSBD[1]
	else:
		print('điền sbd sai')
		cv2.imshow("SBD",getSBD[1])
	print("số báo danh: %s"%sobaodanh)

	# lấy mã đề
	getMaDe = findMaDe()
	madethi = -1
	if getMaDe[0] == True:
		madethi = getMaDe[1]
	else:
		print('điền mã đề sai')
		cv2.imshow("MaDe",getMaDe[1])
	print("mã đề: %s"%madethi)

	# lấy đáp án của học sinh
	getAnswer = findAnswer()
	print('đáp án của học sinh: '+getAnswer[1])

	cv2.waitKey(0)

##############  run  ################
ex1()