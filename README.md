# house-rent(房屋租赁系统)
实际为房屋租赁系统,项目展示为需要改名之后。
## 项目效果展示
![image](https://github.com/junzhumio/house-rent/assets/119744044/cad7189b-79de-4559-95cb-1a140140d2be)
![image](https://github.com/junzhumio/house-rent/assets/119744044/e2e659a5-2cad-4a76-9491-7e15ec564ca4)

## 项目介绍
房屋租赁系统。一个基于区块链的web项目。涉及jwt令牌、redis缓存、智能合约的编写等...

## 项目安装和定义
### 区块链网络搭建
fisco-bcos介绍：https://fisco-bcos-documentation.readthedocs.io/zh-cn/latest/index.html
<br>
fisco-bcos 4节点区块链网络搭建：https://blog.csdn.net/qq_63235624/article/details/130910189
<br>
webase-front 节点前置服务搭建：https://blog.csdn.net/dyw_666666/article/details/124577214
### 合约部署
我们在webase-front节点前置服务页面。
找到合约管理 -> 测试用户: 创建4个测试用户,名称分别是admin、landlord、tenant。(ps:我创建该测试用户是为了分别表示：管理员、房东、租客之间的关系。用户可按照需求自定义)。
![image](https://github.com/junzhumio/house-rent/assets/119744044/60d84543-9209-4c92-a6c9-e140902d4f84)

在合约管理 -> 合约IDE: 新建houseRent目录,点击文件上传按钮,将该包下面的合约文件,全部上传。
![image](https://github.com/junzhumio/house-rent/assets/119744044/1bfb9689-7b3a-4c4f-83e6-bcbdd19d6376)
![image](https://github.com/junzhumio/house-rent/assets/119744044/d8eb5f4b-cf70-41de-94d0-cb32c809096c)


在合约IDE页面,选中MainService合约,合约参数输入上述创建的admin账户的名字(用户可以随意改)。部署成功后,会出现如下图示。

![image](https://github.com/junzhumio/house-rent/assets/119744044/8ca09844-4829-4bd8-97e2-aa58dc2ae62c)

### 后端配置更改
更改redis服务器配置。<br>
更改数据库连接配置。<br>
更改fisco-bcos交互配置:
用IDEA打开back-me项目。将合约部署步骤最后图片所示页面的下部分,contractAddress、contractName、abi、以及部署合约的账户地址(admin地址)、搭建节点前置服务的的服务器地址复制到该项目中。如下面所示:
![image](https://github.com/junzhumio/house-rent/assets/119744044/959b00cc-406b-437d-ad88-56fc4fb03b63)
