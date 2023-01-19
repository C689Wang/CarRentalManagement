package carrental.daointerface;

import java.util.List;
import java.util.Optional;

public interface ObjectDAO<T> {
    public List<T> getAll();
    public Optional<T> getBrand(int id);
    public void addBrand(T object);
}
