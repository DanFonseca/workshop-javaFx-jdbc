package Model.DAO.Impl;

import Model.DAO.DepartmentDAO;
import Model.DB.DB;
import Model.DB.DBException;
import Model.Entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOJDBC implements DepartmentDAO {
    private Connection conn;

    public DepartmentDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement("insert into  department " +
                    "(Name) "+
                    "values (?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, department.getName());

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                rs = st.getGeneratedKeys();
                if(rs.next()){
                    int key = rs.getInt(1);
                    department.setId(key);
                    System.out.println("Rows Affected: " + rowsAffected);
                }else{
                    throw  new DBException("None rows affected");
                }
            }
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement st = null;

        try{
            st = conn.prepareStatement("update department as d "+
                    "set d.Name = ?" +
                    "where " +
                    "d.Id = ?");

            st.setString(1, department.getName());
            st.setInt(2, department.getId());


            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Rows Affected: " + rowsAffected);
            }else{
                throw  new DBException("None rows affected");
            }

        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try{
            st = conn.prepareStatement("delete from department as d where d.Id=? ");
            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Delete Done! Rows Affected: " + rowsAffected);
            }else{
                throw  new DBException("None rows affected");
            }

        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement("select d.Id, d.Name as dpName from department as d " +
                    "where d.Id=?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if(rs.next()){
                return instantiateDepartment(rs);
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
        return null;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {

        Department department = new Department();
        department.setId(rs.getInt("Id"));
        department.setName(rs.getString("dpName"));

        return department;
    }

    @Override
    public List<Department> findAll() {
        Statement st = null;
        ResultSet rs = null;
        List<Department> departmentList = new ArrayList<>();


        try {
            st = conn.createStatement();
            rs = st.executeQuery("select Id, Name from department");
            while (rs.next()){
                departmentList.add(new Department(rs.getInt("Id"),rs.getString("Name")));
            }

            return departmentList;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
        return null;
    }
}
