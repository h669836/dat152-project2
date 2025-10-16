/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	
	public List<User> findAllUsers(){
		
		List<User> allUsers = (List<User>) userRepository.findAll();
		
		return allUsers;
	}
	
	public User findUser(Long userid) throws UserNotFoundException {
		
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: "+userid+" not found"));
		
		return user;
	}
	
	
	// TODO public User saveUser(User user)
    public User saveUser(User user){

        return userRepository.save(user);
    }
	
	// TODO public void deleteUser(Long id) throws UserNotFoundException
    public void deleteUser(Long id) throws UserNotFoundException {
        User user1 = findUser(id);          // Did it this way so that it throws an exception if user is not found

        userRepository.delete(user1);
    }

    // TODO public User updateUser(User user, Long id)
    public User updateUser(User user, Long id) throws UserNotFoundException{
        User updatedUser = findUser(id);

        updatedUser.setFirstname(user.getFirstname());
        updatedUser.setLastname(user.getLastname());
        updatedUser.setOrders(user.getOrders());

        return userRepository.save(updatedUser);
    }

    // TODO public Set<Order> getUserOrders(Long userid)
    public Set<Order> getUserOrders(Long userid) throws UserNotFoundException{
        User user = findUser(userid);

        return user.getOrders();
    }

    // TODO public Order getUserOrder(Long userid, Long oid)
    public Order getUserOrder(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
        User user = findUser(userid);
        Set<Order> orders = user.getOrders();

        return orders.stream()
                .filter(o -> o.getId().equals(oid))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + oid +" for user: " + userid + " not found"));
    }

    // TODO public void deleteOrderForUser(Long userid, Long oid)
    public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
        User user = findUser(userid);
        Set<Order> orders = user.getOrders();

        Order orderToRemove = orders.stream()
                .filter(o -> o.getId().equals(oid))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("Order to delete not found"));

        orders.remove(orderToRemove);

        userRepository.save(user);

    }
	
	// TODO public User createOrdersForUser(Long userid, Order order)
    public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException {
        User user = findUser(userid);

        user.getOrders().add(order);

        return userRepository.save(user);
    }
}
