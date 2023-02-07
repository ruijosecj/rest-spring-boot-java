package ruijosecj.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import ruijosecj.controller.PersonController;
import ruijosecj.data.vo.v1.PersonVO;
import ruijosecj.data.vo.v2.PersonVOV2;
import ruijosecj.exceptions.RequiredObjectIsNullException;
import ruijosecj.exceptions.ResourceNotFoundExcepition;
import ruijosecj.mapper.DozerMapper;
import ruijosecj.mapper.custom.PersonMapper;
import ruijosecj.model.Person;
import ruijosecj.repositories.PersonRepository;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository repository;
	
	@Autowired
	private PersonMapper mapper;
	
	private Logger logger = Logger.getLogger(PersonService.class.getName());

	public PersonVO findById(Long id) {
		logger.info("Finding one PersonVO");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExcepition("No records found for this id: " + id));
		
		var entityVO =  DozerMapper.parseObject(entity, PersonVO.class);
		entityVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return entityVO;
	}
	
	public List<PersonVO> findAll(){
		logger.info("Finding all People");
		
		var persons =  DozerMapper.parseListObjects(repository.findAll(), PersonVO.class) ;
		persons.stream().forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		return persons;
	}

	public PersonVO create(PersonVO personVO) {		
		if (personVO == null) throw new RequiredObjectIsNullException();
		
		logger.info("Creating one PersonVO");
		
		var entity = DozerMapper.parseObject(personVO, Person.class);
		var entityVO = DozerMapper.parseObject( repository.save(entity), PersonVO.class);
		entityVO.add(linkTo(methodOn(PersonController.class).findById(entityVO.getKey())).withSelfRel());
		return entityVO;
	}
	
	public PersonVOV2 createV2(PersonVOV2 personVO) {
		logger.info("Creating one PersonVO V2");
		
		var entity = mapper.convertVoTOEntity(personVO);
		var entityVO = mapper.convertEntityToVo( repository.save(entity));
		return entityVO;
	}
	
	public PersonVO update(PersonVO person) {
		if (person == null) throw new RequiredObjectIsNullException();
		
		logger.info("Updating one PersonVO");
		var personBD = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundExcepition("No records found for this id"));
		
		personBD.setFirstName(person.getFirstName());
		personBD.setLastName(person.getLastName());
		personBD.setAddress(person.getAddress());
		personBD.setGender(person.getGender());
		
		var entityVO = DozerMapper.parseObject( repository.save(personBD), PersonVO.class);
		entityVO.add(linkTo(methodOn(PersonController.class).findById(entityVO.getKey())).withSelfRel());
		return entityVO;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one PersonVO");
		var personBD = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExcepition("No records found for this id"));
		repository.delete(personBD);
	}
}
