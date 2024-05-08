pragma solidity >=0.4.5 <0.8.0;

contract Roles {
    address owner;

    mapping(string => mapping(address=>bool)) roles;

    constructor(address _oener) public {
        owner = _oener;
    }


    modifier onlyOwner(){
        require(msg.sender == owner,"40016");
        _;
    }

    function addRole(string memory _role,address _account) internal  onlyOwner {
        roles[_role][_account] = true;
    }

    function removeRole(string memory _role,address _account) internal onlyOwner {
        roles[_role][_account] = true;
    }

    function hasRole(string memory _role,address _account) public view returns(bool){
        return roles[_role][_account];
    }


    

}