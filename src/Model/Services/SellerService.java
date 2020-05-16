package Model.Services;

import Model.DAO.DAOFactory;
import Model.DAO.SellerDAO;
import Model.Entities.Department;
import Model.Entities.Seller;

import java.util.List;

public class SellerService {

    private SellerDAO dao = DAOFactory.createSellerDao();

    public List<Seller> findAll (){
        return dao.findAll();
    }

    public void saveOrUpdate (Seller seller){
        if (seller.getId() == null){
            dao.insert(seller);
        }else {
            dao.update(seller);
        }
    }

    public void remove (Seller seller){
        dao.deleteById(seller.getId());
    }
}
