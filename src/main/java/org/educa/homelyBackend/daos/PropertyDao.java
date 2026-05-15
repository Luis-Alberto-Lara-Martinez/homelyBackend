package org.educa.homelyBackend.daos;

import org.educa.homelyBackend.models.PropertyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyDao extends JpaRepository<PropertyModel, Integer> {

	@Query("""
			select distinct property
			from PropertyModel property
			left join fetch property.user
			left join fetch property.type
			left join fetch property.status
			left join fetch property.transaction
			left join fetch property.propertyAddress
			left join fetch property.residence
			left join fetch property.propertyImages
			left join fetch property.propertyExtras
			""")
	List<PropertyModel> findAllWithDetails();
}
