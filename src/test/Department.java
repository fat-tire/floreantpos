package test;

import java.util.List;

public class Department {

    private int id;
    private String name;
    private List<Employee> employeeList;

    public Department(int id, String name, List<Employee> empList) {
        this.id = id;
        this.name = name;
        this.employeeList = empList;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;

    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    //other setters and getters
}