package ruijosecj.mapper;

import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DozerMapper {
	
	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();
	
	public static <Origem, Destino> Destino parseObject(Origem origin, Class<Destino> destino) {
		return mapper.map(origin, destino);
	}
	
	public static <Origem, Destino> List<Destino> parseListObjects(List<Origem> origem, Class<Destino> destino) {
		List<Destino> destinoObjects = new ArrayList<Destino>();
		for (Origem o : origem) {
			destinoObjects.add(mapper.map(o, destino));
		}
		return destinoObjects;
	}
}
