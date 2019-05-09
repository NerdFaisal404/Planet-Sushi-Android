package fr.sushi.app.ui.home.data;

import com.google.gson.annotations.SerializedName;


public class Ws{

	@SerializedName("getStoreProducts")
	private GetStoreProducts getStoreProducts;

	@SerializedName("createCustomerAccount")
	private CreateCustomerAccount createCustomerAccount;

	@SerializedName("setDeliveryAddress")
	private SetDeliveryAddress setDeliveryAddress;

	@SerializedName("getAppDatabase")
	private GetAppDatabase getAppDatabase;

	@SerializedName("authentification")
	private Authentification authentification;

	@SerializedName("setTakeawayStore")
	private SetTakeawayStore setTakeawayStore;

	@SerializedName("getAppStores")
	private GetAppStores getAppStores;

	public void setGetStoreProducts(GetStoreProducts getStoreProducts){
		this.getStoreProducts = getStoreProducts;
	}

	public GetStoreProducts getGetStoreProducts(){
		return getStoreProducts;
	}

	public void setCreateCustomerAccount(CreateCustomerAccount createCustomerAccount){
		this.createCustomerAccount = createCustomerAccount;
	}

	public CreateCustomerAccount getCreateCustomerAccount(){
		return createCustomerAccount;
	}

	public void setSetDeliveryAddress(SetDeliveryAddress setDeliveryAddress){
		this.setDeliveryAddress = setDeliveryAddress;
	}

	public SetDeliveryAddress getSetDeliveryAddress(){
		return setDeliveryAddress;
	}

	public void setGetAppDatabase(GetAppDatabase getAppDatabase){
		this.getAppDatabase = getAppDatabase;
	}

	public GetAppDatabase getGetAppDatabase(){
		return getAppDatabase;
	}

	public void setAuthentification(Authentification authentification){
		this.authentification = authentification;
	}

	public Authentification getAuthentification(){
		return authentification;
	}

	public void setSetTakeawayStore(SetTakeawayStore setTakeawayStore){
		this.setTakeawayStore = setTakeawayStore;
	}

	public SetTakeawayStore getSetTakeawayStore(){
		return setTakeawayStore;
	}

	public void setGetAppStores(GetAppStores getAppStores){
		this.getAppStores = getAppStores;
	}

	public GetAppStores getGetAppStores(){
		return getAppStores;
	}

	@Override
 	public String toString(){
		return 
			"Ws{" + 
			"getStoreProducts = '" + getStoreProducts + '\'' + 
			",createCustomerAccount = '" + createCustomerAccount + '\'' + 
			",setDeliveryAddress = '" + setDeliveryAddress + '\'' + 
			",getAppDatabase = '" + getAppDatabase + '\'' + 
			",authentification = '" + authentification + '\'' + 
			",setTakeawayStore = '" + setTakeawayStore + '\'' + 
			",getAppStores = '" + getAppStores + '\'' + 
			"}";
		}
}