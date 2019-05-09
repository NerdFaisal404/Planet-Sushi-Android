package fr.sushi.app.ui.createaccount.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("CustomerAddresses")
	private List<Object> customerAddresses;

	@SerializedName("Customer")
	private Customer customer;

	@SerializedName("token")
	private String token;

	public void setCustomerAddresses(List<Object> customerAddresses){
		this.customerAddresses = customerAddresses;
	}

	public List<Object> getCustomerAddresses(){
		return customerAddresses;
	}

	public void setCustomer(Customer customer){
		this.customer = customer;
	}

	public Customer getCustomer(){
		return customer;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"customerAddresses = '" + customerAddresses + '\'' + 
			",customer = '" + customer + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}