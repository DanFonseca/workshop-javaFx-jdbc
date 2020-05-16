package Model.Services;
import java.util.ArrayList;
import java.util.List;

import Model.DAO.DAOFactory;
import Model.DAO.DepartmentDAO;
import Model.Entities.Department;

public class DepartmentService {

    private DepartmentDAO dao = DAOFactory.createDepartmentDAO();

    public List<Department> findAll (){
        return dao.findAll();
    }

    public void saveOrUpdate (Department department){
        if (department.getId() == null){
            dao.insert(department);
        }else {
            dao.update(department);
        }
    }
}
