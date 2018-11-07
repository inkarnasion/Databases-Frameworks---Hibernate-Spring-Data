

import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private Scanner scanner;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.scanner = new Scanner(System.in);

    }

    public void run() {
        try {
            // Enter number between 2 and 13 and Start selected task!!
            //!!! For some task have to enter addition input
            this.callTask(this.scanner.nextInt());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void callTask(int taskNumber) throws SQLException {

        switch (taskNumber) {
            case 2:
                this.removeObject();
                break;
            case 3:
                this.containsEmployee();
                break;
            case 4:
                this.employeeWithSalaryOver();
                break;
            case 5:
                this.employeesFromDepartment();
                break;
            case 6:
                this.addingNewAddress();
                break;
            case 7:
                this.addressWithEmployeeCount();
                break;
            case 8:
                this.getEmployeeWithProject();
                break;
            case 9:
                this.findLatestProject();
                break;
            case 10:
                this.increaseSalarie();
                break;
            case 11:
                this.removeTowns();
                break;
            case 12:
                this.findEmployeesByFirstName();
                break;
            case 13:
                this.employeesMaximumSalarie();
                break;

            default:
                break;
        }
    }


    /*
    Task 2 - Remove Objects
     */
    private void removeObject() {

        entityManager.getTransaction().begin();

        try {
            List<Town> towns =
                    entityManager.createQuery("SELECT t FROM Town t WHERE LENGTH(t.name) > 5", Town.class)
                            .getResultList();

            towns.forEach(town -> {
                entityManager.detach(town);
                town.setName(town.getName().toLowerCase());
                entityManager.merge(town);
            });
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();

        }
    }


    /*
    Task 3 - Contains Employee
     */
    private void containsEmployee() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] employeeNames = new String[0];
        try {
            employeeNames = reader.readLine().trim().split("\\s+");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Employee> employees = entityManager
                .createQuery("SELECT e FROM Employee AS e WHERE e.firstName = :firstName AND e.lastName = :lastName", Employee.class)
                .setParameter("firstName", employeeNames[0])
                .setParameter("lastName", employeeNames[1])
                .getResultList();

        System.out.println(!employees.isEmpty() ? "Yes" : "No");

        entityManager.close();

    }


    /*
    Task 4 - Employees with Salary Over 50 000
     */
    private void employeeWithSalaryOver() {
        entityManager.getTransaction().begin();

        entityManager
                .createQuery("SELECT e FROM Employee AS e WHERE e.salary > 50000", Employee.class)
                .getResultList().stream().map(Employee::getFirstName).forEach(System.out::println);

        entityManager.close();

    }


    /*
    Task 5 - Employees From Department
     */
    private void employeesFromDepartment() {

        entityManager.getTransaction().begin();

        List<Employee> resultList = entityManager
                .createQuery("SELECT e FROM Employee AS e WHERE e.department.name = 'Research and Development' ORDER BY e.salary, e.id", Employee.class)
                .getResultList();
        for (int i = 0, resultListSize = resultList.size(); i < resultListSize; i++) {
            Employee employee = resultList.get(i);
            System.out.printf("%s %s from %s - $%.2f%n",
                    employee.getFirstName(), employee.getLastName(),
                    employee.getDepartment().getName(), employee.getSalary());
        }

        entityManager.close();

    }


    /*
    Task 6 - Adding a New Address and Updating Employee
     */
    private void addingNewAddress() {

        String employeeName = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            employeeName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        entityManager.getTransaction().begin();

        try {
            Address address = new Address();
            address.setText("Vitoshka 15");
            entityManager.persist(address);

            Employee employee = entityManager.createQuery("SELECT e FROM Employee e WHERE e.lastName =:name", Employee.class)
                    .setParameter("name", employeeName)
                    .getSingleResult();

            employee.setAddress(address);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();

        }
    }


    /*
    Task 7 - Address with Employee Count
     */
    private void addressWithEmployeeCount() {
        entityManager.getTransaction().begin();


        entityManager.createQuery("SELECT a.text, t.name, count(emp) as emp_count FROM Employee as emp " +
                "JOIN emp.address as a " +
                "JOIN a.town as t " +
                "GROUP BY a.text, t.name " +
                "ORDER BY emp_count DESC, t.id, a.id ", Object[].class)
                .setMaxResults(10)
                .getResultList()
                .forEach(x -> System.out.printf("%s, %s - %s employees%n", x[0], x[1], x[2]));

        entityManager.close();
    }


    /*
    Task 8 - Get Employee with Project
     */
    private void getEmployeeWithProject() {
        int id = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            entityManager.getTransaction().begin();


            id = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Employee employee = entityManager.createQuery("SELECT e FROM Employee e WHERE e.id =:id", Employee.class)
                .setParameter("id", id)
                .getSingleResult();

        System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());
        employee.getProjects().stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(project -> System.out.printf("\t%s%n", project.getName()));
        entityManager.getTransaction().commit();
        entityManager.close();

    }


    /*
    Task 9 - Find Latest 10 Projects
     */
    private void findLatestProject() {
        entityManager.getTransaction().begin();


        String hqlQuery = "SELECT p FROM Project AS p ORDER BY p.name ";
        Query query = entityManager.createQuery(hqlQuery).setMaxResults(10);
        List<Project> projects = query.getResultList();

        projects.stream()
                .forEach(p -> {
                    System.out.println(String.format("Project name: %s", p.getName()));
                    System.out.println(String.format("        Project Description: %s", p.getDescription()));
                    System.out.println(String.format("        Project Start Date: %s", p.getStartDate()));
                    System.out.println(String.format("        Project End Date: %s", p.getEndDate()));
                });

    }


    /*
    Task 10 - Increase Salaries
     */
    private void increaseSalarie() {
        entityManager.getTransaction().begin();

        for (Employee employee : entityManager
                .createQuery("SELECT e FROM Employee AS e " +
                        "WHERE e.department.name IN ('Engineering', 'Tool Design', 'Marketing', 'Information Services') " +
                        "ORDER BY e.id", Employee.class)
                .getResultList()) {
            employee.setSalary(employee.getSalary().multiply(BigDecimal.valueOf(1.12)));
            System.out.printf("%s %s($%.2f)%n", employee.getFirstName(),
                    employee.getLastName(), employee.getSalary());
        }

        entityManager.getTransaction().commit();

        entityManager.close();

    }


    /*
    Task 11 - Remove Towns
     */
    private void removeTowns() {

        String townName = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            townName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        entityManager.getTransaction().begin();
        try {
            Town town = entityManager.createQuery("SELECT t FROM Town t WHERE t.name =:name", Town.class)
                    .setParameter("name", townName)
                    .getSingleResult();

            List<Address> addresses = entityManager.createQuery("SELECT a FROM Address a WHERE a.town =:town", Address.class)
                    .setParameter("town", town)
                    .getResultList();

            System.out.printf(addresses.size() == 1
                            ? "%d address in %s deleted%n"
                            : "%d addresses in %s deleted%n",
                    addresses.size(), townName);

            for (Iterator<Address> iterator = addresses.iterator(); iterator.hasNext(); ) {
                Address address = iterator.next();
                for (Iterator<Employee> iterator1 = address.getEmployees().iterator(); iterator1.hasNext(); ) {
                    Employee employee = iterator1.next();
                    employee.setAddress(null);
                }
                entityManager.remove(address);
            }

            entityManager.remove(town);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();

        }
    }


    /*
    Task 12 - Find Employees by First Name
     */
    private void findEmployeesByFirstName() {
        String pattern = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            entityManager.getTransaction().begin();

            pattern = reader.readLine() + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }


        Query findEmployeesQuery = entityManager.createQuery(
                "SELECT e FROM Employee AS e WHERE e.firstName LIKE :pattern");
        findEmployeesQuery.setParameter("pattern", pattern);

        List<Employee> employees = findEmployeesQuery.getResultList();

        employees.forEach(e -> System.out.printf("%s %s - %s - ($%.2f)%n",
                e.getFirstName(),
                e.getLastName(),
                e.getDepartment().getName(),
                e.getSalary()));

    }


    /*
    Task 13 - Employees Maximum Salaries
     */
    private void employeesMaximumSalarie() {
        entityManager.getTransaction().begin();

        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.salary NOT BETWEEN 30000 AND 70000" +
                "GROUP BY e.department.id ORDER BY e.salary DESC", Employee.class)
                .getResultList();

        List<Employee> toSort = new ArrayList<>();
        for (Employee employee : employees) {
            toSort.add(employee);
        }
        toSort.sort(Comparator.comparing(e -> e.getDepartment().getId()));
        for (Employee employee : toSort) {
            System.out.printf("%s - %.2f%n",
                    employee.getDepartment().getName(), employee.getSalary());
        }

        entityManager.close();

    }
}
