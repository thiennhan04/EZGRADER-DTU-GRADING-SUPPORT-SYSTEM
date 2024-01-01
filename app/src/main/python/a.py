def signalTower(n, heights):
    left = [1]*n
    right = [1]*n

    for i in range(1, n):
        j = i-1
        while j >= 0 and heights[j] <= heights[i]:
            left[i] += left[j]
            j -= left[j]

    for i in range(n-2, -1, -1):
        j = i+1
        while j < n and heights[j] <= heights[i]:
            right[i] += right[j]
            j += right[j]

    res = []
    for i in range(n):
        res.append(left[i] + right[i] - 1)

    return res

num_tests = int(input())
for test in range(0,num_tests):
    num_lines = int(input('num_lines: '))
    lines = []
    for j in range(num_lines):
        line = int(input('line: '))
        lines.append(line)

    print(signalTower(len(lines), lines))  # Kết quả: [1, 2, 1, 2, 1]
_ = input()
# 1
# 7
# 100 80 60 70 60 75 85