// SPDX-License-Identifier: SEE LICENSE IN LICENSE
pragma solidity ^0.4.25;
import "./Roles.sol";
contract MainService is Roles{

    constructor(address _owner) public Roles(_owner){

    }

    ///////////////
    //// Type
    ///////////////

    struct Person {
        address account;
        uint balance;
    }

    struct House {
        uint houseId;
        string password;
    }
    struct Contract {
        uint contractId;
        address landlord; // 房东->发布者
        address tenant; // 租客
        uint earnest; // 规定保证金
        uint rent;  // 规定租金
        bool isTenEarnest; // 租客是否缴纳保证金
        bool isTenRent; // 租客是否缴纳租金
        bool isLanEarnest; // 房东是否缴纳保证金
        bool begin; // 是否生效
        bool stop; // 是否终止getContract
        uint tenancyTerm; // 租赁期限 单位: 月份
        uint endTime; // 结束日期 ->合同结束日期
        uint beginTime; // 开始日期 ->合同生效日期
    }

    struct Payment {
        uint paymentId;
        uint contractId;
        uint money;
    }

    ///////////////
    //// Filed
    ///////////////
    uint  constant private  Month = 1 days * 30;
    string  constant private  Role_Landlord = "Landlord";
    string  constant private  Role_Tenant = "Tenant";

    
    mapping(address=>Person) personMap;
    mapping(uint=>House) houseMap;

    mapping(uint=>Contract) signContractMap;
    mapping(uint=>uint) signendTimeMap; // 签署之后,要想生效的截止时间

    mapping (uint=>Payment) paymentMap; 
    uint[] payments;

    ///////////////
    //// Function
    ///////////////

    modifier onlyLandlord() {
        Person memory person = personMap[msg.sender];
        require(hasRole(Role_Landlord,msg.sender),"40014");
        _;
    }

    modifier onlyTenant() {
        require(hasRole(Role_Tenant,msg.sender),"40015");
        _;
    }

    // 添加用户
    function addPerson(address _account,uint8 _role)  public onlyOwner {
        checkEmptyAddress(_account);
        Person memory person;
        if(_role == 2){
            addRole(Role_Landlord,_account);
        }
        if(_role == 3){
            addRole(Role_Tenant,_account);
        }
        person.account = _account;
        personMap[_account] = person;
    }

    // 删除用户角色
    function removeRole(address _account,uint8 _role) public onlyOwner {
            checkEmptyAddress(_account);
            if(_role == 2){
            removeRole(Role_Landlord,_account);
        }
            if(_role == 3){
            removeRole(Role_Tenant,_account);
        }
    }

    

    // 获取用户
    function getPerson(
        address _account
    ) public view returns(uint) {
        Person memory person = personMap[_account];
        return (
            person.balance
        );
    }

    // 充值
    function addBalance(
        address _account,
        uint _amount
    ) public onlyOwner {
        checkAccountExists(_account);  
        Person storage person = personMap[_account];
        person.balance += _amount;
    }

    // 校验账户是否存在
    function checkAccountExists(address _account) public view {
        Person memory person = personMap[_account];
        require( person.account != address(0),"40013");
    }
 

    // 房屋管理------------------
    // 添加房屋
    function addHouse(
        uint _houseId,
        string _password
    ) public onlyLandlord  {
        uint id = _houseId;
        House memory _house = House(
            id,
            _password
        );
        houseMap[id] = _house;
    }

    // 修改密码
    function changeHousePass(
        uint _houseId,
        string _password
    ) public onlyOwner {
        uint id = _houseId;
        houseMap[id].password = _password;
    }

    // 获取房屋信息
    function getHouse(
        uint _houseId,
        uint _contractId
    ) public view  returns (uint,string memory) {
        require(getRealPayRent(_contractId) >= computeTarPayRent(_contractId),"40002");
        House memory _house = houseMap[_houseId];
        return (
            _house.houseId,
            _house.password
        );
    }

    // 获取房屋信息(管理员调用)
    function getHouseAdmin(
        uint _houseId
    )public view onlyOwner  returns (uint,string memory) {
       House memory _house = houseMap[_houseId];
        return (
            _house.houseId,
            _house.password
        );
    }



    // 合同管理------------------
    // 签署合同(这个合同签署,建立在后端合同已经发布,签署的是数据库的合同)
    function signContract(
        uint _contractId,
        address _landlord,
        address _tenant,
        uint _earnest,
        uint _endTime
    ) public {
        uint id = _contractId;
        checkEmptyAddress(_tenant);
        Contract memory nc;
        nc.contractId = _contractId;
        nc.tenant = _tenant;
        nc.landlord = _landlord;
        nc.endTime = _endTime;
        nc.earnest = _earnest;
        nc.rent = _earnest;

        signContractMap[id] = nc;
        signendTimeMap[id] = now() + 1 days;
    }

    // 租客预存赔偿金、租金
    function tenantPrestore(
        address _tenant,
        uint _contractId
    ) public onlyTenant {
        uint id = _contractId;
        Contract storage nc = signContractMap[id];
        uint _money = nc.earnest + nc.rent;
        checkCanPrestore(_tenant,id,_money);
        nc.isTenEarnest = true;
        nc.isTenRent = true;

        personMap[_tenant].balance -= _money;
       
    }

    // 房东预存保证金(合同生效)
    function landlordPrestore(
        address _landlord,
        uint _contractId
    ) public onlyLandlord {
        uint id = _contractId;
        Contract storage nc = signContractMap[id];
        checkCanPrestore(_landlord,id,nc.earnest);
        personMap[_landlord].balance  -= nc.earnest;

        nc.isLanEarnest = true;
        nc.begin = true;
        nc.beginTime = now();
    }

    // 签署期间:退回租客预存的钱
    function backPrestoreMoney(
        uint _contractId,
        address  _tenant
    ) public {
        Contract storage nc = signContractMap[_contractId];
        checkContractTenant(nc, _tenant);
        personMap[_tenant].balance += (nc.earnest + nc.rent); 
    }

    //TODO: 获取合同信息,确定Contract结构在修改
    function getContract(
        uint _contractId
    )  public  view returns (uint,address,address,uint,uint,uint,uint,bool,bool,bool,bool,bool,uint) {
        uint id = _contractId;
        Contract memory nc = signContractMap[id];
        return (
            nc.contractId,nc.landlord,nc.tenant,
            nc.earnest, nc.rent,nc.beginTime,
            nc.endTime,nc.isTenEarnest, nc.isTenRent,
            nc.isLanEarnest,nc.begin,nc.stop,
            signendTimeMap[id]
        );
    }

    //  房东合同终止
    function stopContractLandlord(
        uint _contractId,
        address _landlord
    ) public onlyLandlord {
        Contract memory nc = signContractMap[_contractId];
        checkContractLandLord(nc, _landlord);
        if(now() < nc.endTime){
            address _tenant = nc.tenant;
            uint money1 = getRealPayRent(_contractId) + nc.earnest;
            uint money2 = computeTarPayRent(_contractId);

            if(money1 >= money2){
                personMap[_tenant].balance  += (money1 - money2);
            }else{
                require(true,"40004");
            }
        }
        changeContractStatus(_contractId);
    }

    // 租客合同终止(让租客缴纳的钱 > 违约金,这样永远是租客需要在这个逻辑中接受钱,可以少写很多逻辑!)
    function stopContractTenant(
        uint _contractId,
        address _tenant
    ) public onlyTenant {
        Contract memory nc = signContractMap[_contractId];
        checkContractTenant(nc, _tenant);
        if(now() < nc.endTime){
            uint money1 = getRealPayRent(_contractId);
            uint money2 = computeTarPayRent(_contractId) + (nc.rent * 2);
            if(money1 >= money2){
                personMap[_tenant].balance  += (money1 - money2);
            }else{
                require(true,"40005");
            }
        }
        changeContractStatus(_contractId);
    }
    
    function changeContractStatus(uint _contractId) private {
         Contract storage nc = signContractMap[_contractId];
         nc.begin = false;
         nc.stop = true;
    }


    // 修改合同状态(合同租期必须自然结束)
    function changeContractBegin(
        uint _contractId
    ) public onlyOwner  {
        checkSignContractExists(_contractId);
        Contract storage nc = signContractMap[_contractId];
        require(now() > nc.endTime,"40010");
        nc.begin = false;
    }





    function  checkContractLandLord(
        Contract memory _nc,
        address _landlord
    ) private pure  {
        require(_nc.landlord == _landlord,"40006");    
    }

    function checkContractTenant(
        Contract memory _nc,
        address _tenant
    ) private pure {
        require(_nc.tenant == _tenant,"40003");    
    }

    function checkEmptyAddress(address _account) public pure {
        require(_account != address(0),"40007");
    }

    function checkCanPrestore(
        address _account,
        uint _contractId,
        uint _money
    ) private view {
        uint id = _contractId;
        checkSignContractExists(id);
        checkSignContractExpire(id);
        uint balance = personMap[_account].balance;
        require(balance >= _money,"40008");
    }

    
    function checkSignContractExists(
        uint _contractId
    ) private view {
        Contract memory nc = signContractMap[_contractId];
        require(nc.landlord != address(0),"40011");
    }

    function checkSignContractExpire(
        uint _contractId
    )  private view {
            require(!isSingExpire(_contractId),"40012");
    }


    function isSingExpire(
        uint _contractId
    )   private view returns(bool){
        return signendTimeMap[_contractId] < now();
    }
    
    
    function now() public view returns(uint){
        return block.timestamp/1000;
        // 不清楚,从webasefront拿到的时间戳是ms级时间戳,但是sol官方编译器,拿到是s级时间戳
        // 同一个时间关键字：拿到的都是s为基本单位。
    }





    // 缴纳管理------------------
    // 缴纳租金
    function payRent(
        uint _paymentId,
        uint _contractId,
        uint _money
    ) public onlyTenant {
        checkSignContractExists(_contractId);
        Payment memory payment = Payment(
            _paymentId,
            _contractId,
            _money
        );
        paymentMap[_paymentId] = payment; 
        payments.push(_paymentId);

        address landlord =  signContractMap[_contractId].landlord; 
        personMap[landlord].balance += _money;
    }


    // 获取缴纳实体信息
    function getPayRent(
          uint _paymentId
    ) public view returns(uint,uint,uint) {
        Payment memory payment = paymentMap[_paymentId];
        return (
            payment.paymentId,
            payment.contractId,
            payment.money
        );
    }


    // 计算目标缴纳租金:已经租了几个月,计算几个月的金额
    function computeTarPayRent(
        uint _contractId
    )private view returns (uint){
        uint num =  computeTerm(_contractId);
        Contract memory c = signContractMap[_contractId];
        return num * c.earnest;
    }
    

    function computeTerm(
        uint _contractId
    ) private view  returns (uint8) {
        uint nowT = now();
        Contract memory c = signContractMap[_contractId];
        return uint8((nowT - c.beginTime) /  Month);
    }

    function getRealPayRent(
        uint _contractId
    ) private view returns (uint) {
        uint total = 0;
        for (uint i = 0; i < payments.length; i++) {
            Payment memory p = paymentMap[payments[i]];
            if(p.contractId == _contractId){
                total += p.money;
            }
        }
        return total;
    }



}