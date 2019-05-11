package fr.sushi.app.ui.checkout.commade.model;

import com.google.gson.annotations.SerializedName;


public class PayingSaucesItem{

	@SerializedName("active_sauce_picture")
	private boolean activeSaucePicture;

	@SerializedName("category_name")
	private String categoryName;

	@SerializedName("only_bynight")
	private String onlyBynight;

	@SerializedName("titre_seo")
	private String titreSeo;

	@SerializedName("id_category")
	private String idCategory;

	@SerializedName("actif_webmobile_picture")
	private boolean actifWebmobilePicture;

	@SerializedName("link")
	private String link;

	@SerializedName("footer_seo")
	private String footerSeo;

	@SerializedName("active_sauce")
	private String activeSauce;

	@SerializedName("description")
	private String description;

	@SerializedName("html_name")
	private String htmlName;

	@SerializedName("active_delivery")
	private String activeDelivery;

	@SerializedName("active_criteo")
	private String activeCriteo;

	@SerializedName("reference")
	private String reference;

	@SerializedName("id_lang")
	private String idLang;

	@SerializedName("description_seo")
	private String descriptionSeo;

	@SerializedName("public")
	private String jsonMemberPublic;

	@SerializedName("id_product_lang")
	private String idProductLang;

	@SerializedName("id_root_category")
	private String idRootCategory;

	@SerializedName("price_ht")
	private String priceHt;

	@SerializedName("id_category_default")
	private String idCategoryDefault;

	@SerializedName("id_product_picture")
	private String idProductPicture;

	@SerializedName("link_rewrite")
	private String linkRewrite;

	@SerializedName("notice")
	private String notice;

	@SerializedName("id_category_product")
	private String idCategoryProduct;

	@SerializedName("id_product_picture_app")
	private String idProductPictureApp;

	@SerializedName("description_secondary")
	private String descriptionSecondary;

	@SerializedName("cover_url")
	private String coverUrl;

	@SerializedName("active_cross_selling")
	private int activeCrossSelling;

	@SerializedName("active_criteo_picture")
	private boolean activeCriteoPicture;

	@SerializedName("active")
	private String active;

	@SerializedName("code_ventilation")
	private String codeVentilation;

	@SerializedName("only_am")
	private String onlyAm;

	@SerializedName("date_add")
	private String dateAdd;

	@SerializedName("contains_alcohol")
	private String containsAlcohol;

	@SerializedName("actif_webmobile")
	private String actifWebmobile;

	@SerializedName("description_short")
	private String descriptionShort;

	@SerializedName("id_product")
	private String idProduct;

	@SerializedName("id_product_picture_marketing")
	private boolean idProductPictureMarketing;

	@SerializedName("public_picture")
	private boolean publicPicture;

	@SerializedName("active_vae")
	private String activeVae;

	@SerializedName("date_upd")
	private String dateUpd;

	@SerializedName("name")
	private String name;

	@SerializedName("position")
	private String position;

	@SerializedName("active_up_selling")
	private int activeUpSelling;

	@SerializedName("id_taxe")
	private String idTaxe;

	@SerializedName("price_ttc")
	private String priceTtc;

	public void setActiveSaucePicture(boolean activeSaucePicture){
		this.activeSaucePicture = activeSaucePicture;
	}

	public boolean isActiveSaucePicture(){
		return activeSaucePicture;
	}

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public void setOnlyBynight(String onlyBynight){
		this.onlyBynight = onlyBynight;
	}

	public String getOnlyBynight(){
		return onlyBynight;
	}

	public void setTitreSeo(String titreSeo){
		this.titreSeo = titreSeo;
	}

	public String getTitreSeo(){
		return titreSeo;
	}

	public void setIdCategory(String idCategory){
		this.idCategory = idCategory;
	}

	public String getIdCategory(){
		return idCategory;
	}

	public void setActifWebmobilePicture(boolean actifWebmobilePicture){
		this.actifWebmobilePicture = actifWebmobilePicture;
	}

	public boolean isActifWebmobilePicture(){
		return actifWebmobilePicture;
	}

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	public void setFooterSeo(String footerSeo){
		this.footerSeo = footerSeo;
	}

	public String getFooterSeo(){
		return footerSeo;
	}

	public void setActiveSauce(String activeSauce){
		this.activeSauce = activeSauce;
	}

	public String getActiveSauce(){
		return activeSauce;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setHtmlName(String htmlName){
		this.htmlName = htmlName;
	}

	public String getHtmlName(){
		return htmlName;
	}

	public void setActiveDelivery(String activeDelivery){
		this.activeDelivery = activeDelivery;
	}

	public String getActiveDelivery(){
		return activeDelivery;
	}

	public void setActiveCriteo(String activeCriteo){
		this.activeCriteo = activeCriteo;
	}

	public String getActiveCriteo(){
		return activeCriteo;
	}

	public void setReference(String reference){
		this.reference = reference;
	}

	public String getReference(){
		return reference;
	}

	public void setIdLang(String idLang){
		this.idLang = idLang;
	}

	public String getIdLang(){
		return idLang;
	}

	public void setDescriptionSeo(String descriptionSeo){
		this.descriptionSeo = descriptionSeo;
	}

	public String getDescriptionSeo(){
		return descriptionSeo;
	}

	public void setJsonMemberPublic(String jsonMemberPublic){
		this.jsonMemberPublic = jsonMemberPublic;
	}

	public String getJsonMemberPublic(){
		return jsonMemberPublic;
	}

	public void setIdProductLang(String idProductLang){
		this.idProductLang = idProductLang;
	}

	public String getIdProductLang(){
		return idProductLang;
	}

	public void setIdRootCategory(String idRootCategory){
		this.idRootCategory = idRootCategory;
	}

	public String getIdRootCategory(){
		return idRootCategory;
	}

	public void setPriceHt(String priceHt){
		this.priceHt = priceHt;
	}

	public String getPriceHt(){
		return priceHt;
	}

	public void setIdCategoryDefault(String idCategoryDefault){
		this.idCategoryDefault = idCategoryDefault;
	}

	public String getIdCategoryDefault(){
		return idCategoryDefault;
	}

	public void setIdProductPicture(String idProductPicture){
		this.idProductPicture = idProductPicture;
	}

	public String getIdProductPicture(){
		return idProductPicture;
	}

	public void setLinkRewrite(String linkRewrite){
		this.linkRewrite = linkRewrite;
	}

	public String getLinkRewrite(){
		return linkRewrite;
	}

	public void setNotice(String notice){
		this.notice = notice;
	}

	public String getNotice(){
		return notice;
	}

	public void setIdCategoryProduct(String idCategoryProduct){
		this.idCategoryProduct = idCategoryProduct;
	}

	public String getIdCategoryProduct(){
		return idCategoryProduct;
	}

	public void setIdProductPictureApp(String idProductPictureApp){
		this.idProductPictureApp = idProductPictureApp;
	}

	public String getIdProductPictureApp(){
		return idProductPictureApp;
	}

	public void setDescriptionSecondary(String descriptionSecondary){
		this.descriptionSecondary = descriptionSecondary;
	}

	public String getDescriptionSecondary(){
		return descriptionSecondary;
	}

	public void setCoverUrl(String coverUrl){
		this.coverUrl = coverUrl;
	}

	public String getCoverUrl(){
		return coverUrl;
	}

	public void setActiveCrossSelling(int activeCrossSelling){
		this.activeCrossSelling = activeCrossSelling;
	}

	public int getActiveCrossSelling(){
		return activeCrossSelling;
	}

	public void setActiveCriteoPicture(boolean activeCriteoPicture){
		this.activeCriteoPicture = activeCriteoPicture;
	}

	public boolean isActiveCriteoPicture(){
		return activeCriteoPicture;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setCodeVentilation(String codeVentilation){
		this.codeVentilation = codeVentilation;
	}

	public String getCodeVentilation(){
		return codeVentilation;
	}

	public void setOnlyAm(String onlyAm){
		this.onlyAm = onlyAm;
	}

	public String getOnlyAm(){
		return onlyAm;
	}

	public void setDateAdd(String dateAdd){
		this.dateAdd = dateAdd;
	}

	public String getDateAdd(){
		return dateAdd;
	}

	public void setContainsAlcohol(String containsAlcohol){
		this.containsAlcohol = containsAlcohol;
	}

	public String getContainsAlcohol(){
		return containsAlcohol;
	}

	public void setActifWebmobile(String actifWebmobile){
		this.actifWebmobile = actifWebmobile;
	}

	public String getActifWebmobile(){
		return actifWebmobile;
	}

	public void setDescriptionShort(String descriptionShort){
		this.descriptionShort = descriptionShort;
	}

	public String getDescriptionShort(){
		return descriptionShort;
	}

	public void setIdProduct(String idProduct){
		this.idProduct = idProduct;
	}

	public String getIdProduct(){
		return idProduct;
	}

	public void setIdProductPictureMarketing(boolean idProductPictureMarketing){
		this.idProductPictureMarketing = idProductPictureMarketing;
	}

	public boolean isIdProductPictureMarketing(){
		return idProductPictureMarketing;
	}

	public void setPublicPicture(boolean publicPicture){
		this.publicPicture = publicPicture;
	}

	public boolean isPublicPicture(){
		return publicPicture;
	}

	public void setActiveVae(String activeVae){
		this.activeVae = activeVae;
	}

	public String getActiveVae(){
		return activeVae;
	}

	public void setDateUpd(String dateUpd){
		this.dateUpd = dateUpd;
	}

	public String getDateUpd(){
		return dateUpd;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPosition(String position){
		this.position = position;
	}

	public String getPosition(){
		return position;
	}

	public void setActiveUpSelling(int activeUpSelling){
		this.activeUpSelling = activeUpSelling;
	}

	public int getActiveUpSelling(){
		return activeUpSelling;
	}

	public void setIdTaxe(String idTaxe){
		this.idTaxe = idTaxe;
	}

	public String getIdTaxe(){
		return idTaxe;
	}

	public void setPriceTtc(String priceTtc){
		this.priceTtc = priceTtc;
	}

	public String getPriceTtc(){
		return priceTtc;
	}

	@Override
 	public String toString(){
		return 
			"PayingSaucesItem{" + 
			"active_sauce_picture = '" + activeSaucePicture + '\'' + 
			",category_name = '" + categoryName + '\'' + 
			",only_bynight = '" + onlyBynight + '\'' + 
			",titre_seo = '" + titreSeo + '\'' + 
			",id_category = '" + idCategory + '\'' + 
			",actif_webmobile_picture = '" + actifWebmobilePicture + '\'' + 
			",link = '" + link + '\'' + 
			",footer_seo = '" + footerSeo + '\'' + 
			",active_sauce = '" + activeSauce + '\'' + 
			",description = '" + description + '\'' + 
			",html_name = '" + htmlName + '\'' + 
			",active_delivery = '" + activeDelivery + '\'' + 
			",active_criteo = '" + activeCriteo + '\'' + 
			",reference = '" + reference + '\'' + 
			",id_lang = '" + idLang + '\'' + 
			",description_seo = '" + descriptionSeo + '\'' + 
			",public = '" + jsonMemberPublic + '\'' + 
			",id_product_lang = '" + idProductLang + '\'' + 
			",id_root_category = '" + idRootCategory + '\'' + 
			",price_ht = '" + priceHt + '\'' + 
			",id_category_default = '" + idCategoryDefault + '\'' + 
			",id_product_picture = '" + idProductPicture + '\'' + 
			",link_rewrite = '" + linkRewrite + '\'' + 
			",notice = '" + notice + '\'' + 
			",id_category_product = '" + idCategoryProduct + '\'' + 
			",id_product_picture_app = '" + idProductPictureApp + '\'' + 
			",description_secondary = '" + descriptionSecondary + '\'' + 
			",cover_url = '" + coverUrl + '\'' + 
			",active_cross_selling = '" + activeCrossSelling + '\'' + 
			",active_criteo_picture = '" + activeCriteoPicture + '\'' + 
			",active = '" + active + '\'' + 
			",code_ventilation = '" + codeVentilation + '\'' + 
			",only_am = '" + onlyAm + '\'' + 
			",date_add = '" + dateAdd + '\'' + 
			",contains_alcohol = '" + containsAlcohol + '\'' + 
			",actif_webmobile = '" + actifWebmobile + '\'' + 
			",description_short = '" + descriptionShort + '\'' + 
			",id_product = '" + idProduct + '\'' + 
			",id_product_picture_marketing = '" + idProductPictureMarketing + '\'' + 
			",public_picture = '" + publicPicture + '\'' + 
			",active_vae = '" + activeVae + '\'' + 
			",date_upd = '" + dateUpd + '\'' + 
			",name = '" + name + '\'' + 
			",position = '" + position + '\'' + 
			",active_up_selling = '" + activeUpSelling + '\'' + 
			",id_taxe = '" + idTaxe + '\'' + 
			",price_ttc = '" + priceTtc + '\'' + 
			"}";
		}
}