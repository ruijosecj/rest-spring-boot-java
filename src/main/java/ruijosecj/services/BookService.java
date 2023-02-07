package ruijosecj.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ruijosecj.controller.BookController;
import ruijosecj.data.vo.v1.BookVO;
import ruijosecj.exceptions.RequiredObjectIsNullException;
import ruijosecj.exceptions.ResourceNotFoundExcepition;
import ruijosecj.mapper.DozerMapper;
import ruijosecj.model.Book;
import ruijosecj.repositories.BookRepository;

@Service
public class BookService {
	
	@Autowired
	private BookRepository repository;
	
	private Logger logger = Logger.getLogger(BookService.class.getName());

	public BookVO findById(Long id) {
		logger.info("Finding one BookVO");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExcepition("No records found for this id: " + id));
		
		var entityVO =  DozerMapper.parseObject(entity, BookVO.class);
		entityVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return entityVO;
	}
	
	public List<BookVO> findAll(){
		logger.info("Finding all Book");
		
		var books =  DozerMapper.parseListObjects(repository.findAll(), BookVO.class) ;
		books.stream().forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
		return books;
	}

	public BookVO create(BookVO bookVO) {		
		if (bookVO == null) throw new RequiredObjectIsNullException();
		
		logger.info("Creating one BookVO");
		
		var entity = DozerMapper.parseObject(bookVO, Book.class);
		var entityVO = DozerMapper.parseObject( repository.save(entity), BookVO.class);
		entityVO.add(linkTo(methodOn(BookController.class).findById(entityVO.getKey())).withSelfRel());
		return entityVO;
	}
	
//	public BookVOV2 createV2(BookVOV2 bookVO) {
//		logger.info("Creating one BookVO V2");
//		
//		var entity = mapper.convertVoTOEntity(bookVO);
//		var entityVO = mapper.convertEntityToVo( repository.save(entity));
//		return entityVO;
//	}
	
	public BookVO update(BookVO book) {
		if (book == null) throw new RequiredObjectIsNullException();
		
		logger.info("Updating one BookVO");
		var bookBD = repository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundExcepition("No records found for this id"));
		
		bookBD.setAuthor(book.getAuthor());
		bookBD.setLaunchDate(book.getLaunchDate());
		bookBD.setPrice(book.getPrice());
		bookBD.setTitle(book.getTitle());
		
		var entityVO = DozerMapper.parseObject( repository.save(bookBD), BookVO.class);
		entityVO.add(linkTo(methodOn(BookController.class).findById(entityVO.getKey())).withSelfRel());
		return entityVO;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one BookVO");
		var bookBD = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExcepition("No records found for this id"));
		repository.delete(bookBD);
	}
}
