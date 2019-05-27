package fr.sushi.app.ui.checkout.paiement.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("CartDiscounts")
	private List<CartDiscountsItem> cartDiscounts;

	@SerializedName("id_cart")
	private int idCart;

	public void setCartDiscounts(List<CartDiscountsItem> cartDiscounts){
		this.cartDiscounts = cartDiscounts;
	}

	public List<CartDiscountsItem> getCartDiscounts(){
		return cartDiscounts;
	}

	public void setIdCart(int idCart){
		this.idCart = idCart;
	}

	public int getIdCart(){
		return idCart;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"cartDiscounts = '" + cartDiscounts + '\'' + 
			",id_cart = '" + idCart + '\'' + 
			"}";
		}
}