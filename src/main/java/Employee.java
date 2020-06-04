public class Employee {
  String employeeID, name, age, salary, address;
  
  public Employee() {
  }
  
  public Employee(String employeeID, String name, String age, String salary, String address) {
    this.employeeID = employeeID;
    this.name = name;
    this.age = age;
    this.salary = salary;
    this.address = address;
  }
  
  public String getEmployeeID() {
    return employeeID;
  }
  
  public void setEmployeeID(String employeeID) {
    this.employeeID = employeeID;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getAge() {
    return age;
  }
  
  public void setAge(String age) {
    this.age = age;
  }
  
  public String getSalary() {
    return salary;
  }
  
  public void setSalary(String salary) {
    this.salary = salary;
  }
  
  public String getAddress() {
    return address;
  }
  
  public void setAddress(String address) {
    this.address = address;
  }
}
