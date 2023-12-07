#include <iostream>

int factorial(int n) {
    if (n == 0 || n == 1) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
}

int main() {
    int num;
    std::cout << "请输入一个整数：";
    std::cin >> num;
    std::cout << num << "的阶乘是：" << factorial(num) << std::endl;
    return 0;
}
