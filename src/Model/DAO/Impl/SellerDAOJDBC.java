package Model.DAO.Impl;

import Model.DB.*;
import Model.DAO.SellerDAO;
import Model.Entities.Department;
import Model.Entities.Seller;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SellerDAOJDBC implements SellerDAO {
    private Connection conn;

    public SellerDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement("insert into  seller" +
                    "(Name,Email,BirthDate,BaseSalary,DepartmentId) " +
                    "values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3,  new Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                rs = st.getGeneratedKeys();
                if(rs.next()){
                    int key = rs.getInt(1);
                    seller.setId(key);
                    System.out.println("Done! Rows Affected: " + rowsAffected);
                }
            }else{
                throw  new DBException("None rows affected");
            }

        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;
        ResultSet rs = null;


        try{
            st = conn.prepareStatement("UPDATE  seller as s " +
                    "set "+
                    "s.name= ?, " +
                    "s.Email = ?, "+
                    "s.BirthDate = ?, "+
                    "s.BaseSalary  = ?, "+
                    "DepartmentId = ? "+
                    "where  s.Id = ?");

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3,  new Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());

            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Done! Rows Affected: " + rowsAffected);
            }else{
                throw  new DBException("None rows affected");
            }

        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try{
            st = conn.prepareStatement("delete from seller as s " +
                    " where s.Id = ?");
            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Done! Rows Affected: " + rowsAffected);
            }else{
                throw new DBException("None rows affected");
            }
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public Seller findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            st = conn.prepareStatement("select s.*, d.Id, d.Name as dpName " +
                    "FROM seller as s " +
                    "inner join department as d " +
                    "on s.DepartmentId = d.Id " +
                    "where s.Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Department department = instantiateDepartment(rs);
                Seller seller = instantiateSeller(rs, department);
                return seller;
            }
            return null;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Seller> sellerList = new ArrayList<>();

        try {
            st = conn.prepareStatement("select s.* , d.Name as dpName " +
                    "from seller as s " +
                    "inner join department as d " +
                    "on s.DepartmentId = d.Id " +
                    "WHERE d.Id = ? " +
                    "order by s.Name");

            st.setInt(1, department.getId());
            rs = st.executeQuery();

            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(dep.getId(), dep);
                }
                sellerList.add(instantiateSeller(rs, dep));
            }


        } catch (SQLException e) {
            e.getStackTrace();
            throw new DBException(e.getMessage());
        }

        return sellerList;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {

        Department department = new Department();
        department.setId(rs.getInt("DepartmentId"));
        department.setName(rs.getString("dpName"));

        return department;
    }

    private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException {

        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
       // seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setBirthDate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(department);

        return seller;
    }

    @Override
    public List<Seller> findAll() {
        Statement st = null;
        ResultSet rs = null;
        List<Seller> sellerList = new ArrayList<>();
        Map<Integer, Department> map = new HashMap<>();

        try {
            st = conn.createStatement();
            rs = st.executeQuery("select s.*, d.Name as dpName " +
                            "from seller as s " +
                            "inner join department as d " +
                            "on d.Id = s.DepartmentId");

            while (rs.next()) {
                //ver se o departamente existe no map
                Department department = department = map.get(rs.getInt("DepartmentId"));
                //se nao existir, instanciar um novo departamento
                if (department == null) {
                    department = instantiateDepartment(rs);
                    map.put(department.getId(), department);
                }
                //criando seller passando department
                sellerList.add(instantiateSeller(rs, department));
            }
            return sellerList;

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
        return null;
    }

}
