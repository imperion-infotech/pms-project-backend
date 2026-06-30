package com.pms.taxmaster.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pms.floor.dao.impl.FloorDAOImpl;
import com.pms.floor.entity.Floor;
import com.pms.room.entity.RoomMaster;
import com.pms.taxmaster.dao.ITaxMasterDAO;
import com.pms.taxmaster.entity.TaxMaster;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Transactional
@Repository
public class TaxMasterDAOImpl implements ITaxMasterDAO {

static final Logger logger = LoggerFactory.getLogger(TaxMasterDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<TaxMaster> getTaxMasters() {
		String hql = "FROM TaxMaster as atcl ORDER BY atcl.id";
		return (List<TaxMaster>) entityManager.createQuery(hql).getResultList();
	}
	

	public TaxMaster getTaxMaster(Long taxMasterId) {
		return entityManager.find(TaxMaster.class, taxMasterId);
	}

	public TaxMaster createTaxMaster(TaxMaster taxMaster) {
		entityManager.persist(taxMaster);
		TaxMaster b = getLastInsertedTaxMaster();
		return b;
	}

	public TaxMaster updateTaxMaster(Long taxMasterId, TaxMaster taxMaster) {
		//First We are taking Book detail from database by given book id and 
				// then updating detail with provided book object
				TaxMaster taxMasterFromDB = getTaxMaster(taxMasterId);
				taxMasterFromDB.setTaxMasterName(taxMaster.getTaxMasterName());
				taxMasterFromDB.setTaxTypeEnum(taxMaster.getTaxTypeEnum());
				taxMasterFromDB.setPerDayTax(taxMaster.getPerDayTax());
				taxMasterFromDB.setPerStayTax(taxMaster.getPerStayTax());
				entityManager.flush();
				
				//again i am taking updated result of book and returning the book object
				TaxMaster updatedTaxMaster = getTaxMaster(taxMasterId);
				
				return updatedTaxMaster;
	}

	public boolean deleteTaxMaster(Long taxMasterId) {
		TaxMaster taxMaster = getTaxMaster(taxMasterId);
		entityManager.remove(taxMaster);
		
		//we are checking here that whether entityManager contains earlier deleted book or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(taxMaster);
		if(status){
			return false;
		}
		return true;
	}

	
	/**
	 * This method will get the latest inserted record from the database and return the object of Book class
	 * @return book
	 */
	private TaxMaster getLastInsertedTaxMaster(){
		String hql = "from TaxMaster order by id DESC";
		Query query = entityManager.createQuery(hql);
		query.setMaxResults(1);
		TaxMaster taxMaster = (TaxMaster)query.getSingleResult();
		return taxMaster;
	}

	@Override
	public TaxMaster findById(Integer id) {
		        return entityManager.find(TaxMaster.class, id);
		    }


	}
