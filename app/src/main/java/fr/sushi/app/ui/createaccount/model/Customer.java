package fr.sushi.app.ui.createaccount.model;

import com.google.gson.annotations.SerializedName;

public class Customer{

	@SerializedName("birthday")
	private String birthday;

	@SerializedName("newsletter")
	private int newsletter;

	@SerializedName("firstname")
	private String firstname;

	@SerializedName("id_store_pm")
	private int idStorePm;

	@SerializedName("is_new")
	private int isNew;

	@SerializedName("id_customer")
	private int idCustomer;

	@SerializedName("optin")
	private int optin;

	@SerializedName("active")
	private int active;

	@SerializedName("lastname")
	private String lastname;

	@SerializedName("reference")
	private String reference;

	@SerializedName("date_add")
	private String dateAdd;

	@SerializedName("id_address")
	private int idAddress;

	@SerializedName("id_store_am")
	private int idStoreAm;

	@SerializedName("password")
	private String password;

	@SerializedName("deleted")
	private int deleted;

	@SerializedName("phone")
	private String phone;

	@SerializedName("date_upd")
	private String dateUpd;

	@SerializedName("id_gender")
	private int idGender;

	@SerializedName("id_customer_group")
	private int idCustomerGroup;

	@SerializedName("email")
	private String email;

	@SerializedName("push_token")
	private String pushToken;

	public void setBirthday(String birthday){
		this.birthday = birthday;
	}

	public String getBirthday(){
		return birthday;
	}

	public void setNewsletter(int newsletter){
		this.newsletter = newsletter;
	}

	public int getNewsletter(){
		return newsletter;
	}

	public void setFirstname(String firstname){
		this.firstname = firstname;
	}

	public String getFirstname(){
		return firstname;
	}

	public void setIdStorePm(int idStorePm){
		this.idStorePm = idStorePm;
	}

	public int getIdStorePm(){
		return idStorePm;
	}

	public void setIsNew(int isNew){
		this.isNew = isNew;
	}

	public int getIsNew(){
		return isNew;
	}

	public void setIdCustomer(int idCustomer){
		this.idCustomer = idCustomer;
	}

	public int getIdCustomer(){
		return idCustomer;
	}

	public void setOptin(int optin){
		this.optin = optin;
	}

	public int getOptin(){
		return optin;
	}

	public void setActive(int active){
		this.active = active;
	}

	public int getActive(){
		return active;
	}

	public void setLastname(String lastname){
		this.lastname = lastname;
	}

	public String getLastname(){
		return lastname;
	}

	public void setReference(String reference){
		this.reference = reference;
	}

	public String getReference(){
		return reference;
	}

	public void setDateAdd(String dateAdd){
		this.dateAdd = dateAdd;
	}

	public String getDateAdd(){
		return dateAdd;
	}

	public void setIdAddress(int idAddress){
		this.idAddress = idAddress;
	}

	public int getIdAddress(){
		return idAddress;
	}

	public void setIdStoreAm(int idStoreAm){
		this.idStoreAm = idStoreAm;
	}

	public int getIdStoreAm(){
		return idStoreAm;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setDeleted(int deleted){
		this.deleted = deleted;
	}

	public int getDeleted(){
		return deleted;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setDateUpd(String dateUpd){
		this.dateUpd = dateUpd;
	}

	public String getDateUpd(){
		return dateUpd;
	}

	public void setIdGender(int idGender){
		this.idGender = idGender;
	}

	public int getIdGender(){
		return idGender;
	}

	public void setIdCustomerGroup(int idCustomerGroup){
		this.idCustomerGroup = idCustomerGroup;
	}

	public int getIdCustomerGroup(){
		return idCustomerGroup;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setPushToken(String pushToken){
		this.pushToken = pushToken;
	}

	public String getPushToken(){
		return pushToken;
	}

	@Override
 	public String toString(){
		return 
			"Customer{" + 
			"birthday = '" + birthday + '\'' + 
			",newsletter = '" + newsletter + '\'' + 
			",firstname = '" + firstname + '\'' + 
			",id_store_pm = '" + idStorePm + '\'' + 
			",is_new = '" + isNew + '\'' + 
			",id_customer = '" + idCustomer + '\'' + 
			",optin = '" + optin + '\'' + 
			",active = '" + active + '\'' + 
			",lastname = '" + lastname + '\'' + 
			",reference = '" + reference + '\'' + 
			",date_add = '" + dateAdd + '\'' + 
			",id_address = '" + idAddress + '\'' + 
			",id_store_am = '" + idStoreAm + '\'' + 
			",password = '" + password + '\'' + 
			",deleted = '" + deleted + '\'' + 
			",phone = '" + phone + '\'' + 
			",date_upd = '" + dateUpd + '\'' + 
			",id_gender = '" + idGender + '\'' + 
			",id_customer_group = '" + idCustomerGroup + '\'' + 
			",email = '" + email + '\'' + 
			",push_token = '" + pushToken + '\'' + 
			"}";
		}
}