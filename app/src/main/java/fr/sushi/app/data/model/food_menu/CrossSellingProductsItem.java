package fr.sushi.app.data.model.food_menu;

import com.google.gson.annotations.SerializedName;

public class CrossSellingProductsItem {

    @SerializedName("active_sauce_picture")
    private boolean activeSaucePicture;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("picture_url")
    private String pictureUrl;

    @SerializedName("only_bynight")
    private String onlyBynight;

    @SerializedName("footer_seo")
    private String footerSeo;

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

    @SerializedName("id_root_category")
    private String idRootCategory;

    @SerializedName("price_ht")
    private String priceHt;

    @SerializedName("id_category_product")
    private String idCategoryProduct;

    @SerializedName("id_product_picture_app")
    private String idProductPictureApp;

    @SerializedName("active_criteo_picture")
    private boolean activeCriteoPicture;

    @SerializedName("active")
    private String active;

    @SerializedName("only_am")
    private String onlyAm;

    @SerializedName("nbre_piece_picture")
    private boolean nbrePiecePicture;

    @SerializedName("contains_alcohol")
    private String containsAlcohol;

    @SerializedName("kcal")
    private String kcal;

    @SerializedName("id_product")
    private String idProduct;

    @SerializedName("public_picture")
    private boolean publicPicture;

    @SerializedName("active_vae")
    private String activeVae;

    @SerializedName("name")
    private String name;

    @SerializedName("position")
    private String position;

    @SerializedName("id_taxe")
    private String idTaxe;

    @SerializedName("price_ttc")
    private String priceTtc;

    @SerializedName("kcal_picture")
    private boolean kcalPicture;

    @SerializedName("nbre_piece")
    private String nbrePiece;

    @SerializedName("titre_seo")
    private String titreSeo;

    @SerializedName("id_category")
    private String idCategory;

    @SerializedName("actif_webmobile_picture")
    private boolean actifWebmobilePicture;

    @SerializedName("link")
    private String link;

    @SerializedName("active_sauce")
    private String activeSauce;

    @SerializedName("description")
    private String description;

    @SerializedName("description_seo")
    private String descriptionSeo;

    @SerializedName("public")
    private String jsonMemberPublic;

    @SerializedName("id_product_lang")
    private String idProductLang;

    @SerializedName("id_category_default")
    private String idCategoryDefault;

    @SerializedName("id_product_picture")
    private String idProductPicture;

    @SerializedName("link_rewrite")
    private String linkRewrite;

    @SerializedName("notice")
    private String notice;

    @SerializedName("description_secondary")
    private String descriptionSecondary;

    @SerializedName("cover_url")
    private String coverUrl;

    @SerializedName("active_cross_selling")
    private int activeCrossSelling;

    @SerializedName("code_ventilation")
    private String codeVentilation;

    @SerializedName("date_add")
    private String dateAdd;

    @SerializedName("actif_webmobile")
    private String actifWebmobile;

    @SerializedName("description_short")
    private String descriptionShort;

    @SerializedName("id_product_picture_marketing")
    private boolean idProductPictureMarketing;

    @SerializedName("date_upd")
    private String dateUpd;

    @SerializedName("active_up_selling")
    private int activeUpSelling;

    private String headerTitle;
    private String headerDescription;

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderDescription() {
        return headerDescription;
    }

    public void setHeaderDescription(String headerDescription) {
        this.headerDescription = headerDescription;
    }

    // below item for cross selling adapter
    private int maxCount;

    int itemClickCount;

    boolean isFree;

    boolean isRequired;

    public void setActiveSaucePicture(boolean activeSaucePicture) {
        this.activeSaucePicture = activeSaucePicture;
    }

    public boolean isActiveSaucePicture() {
        return activeSaucePicture;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setOnlyBynight(String onlyBynight) {
        this.onlyBynight = onlyBynight;
    }

    public String getOnlyBynight() {
        return onlyBynight;
    }

    public void setFooterSeo(String footerSeo) {
        this.footerSeo = footerSeo;
    }

    public String getFooterSeo() {
        return footerSeo;
    }

    public void setHtmlName(String htmlName) {
        this.htmlName = htmlName;
    }

    public String getHtmlName() {
        return htmlName;
    }

    public void setActiveDelivery(String activeDelivery) {
        this.activeDelivery = activeDelivery;
    }

    public String getActiveDelivery() {
        return activeDelivery;
    }

    public void setActiveCriteo(String activeCriteo) {
        this.activeCriteo = activeCriteo;
    }

    public String getActiveCriteo() {
        return activeCriteo;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setIdLang(String idLang) {
        this.idLang = idLang;
    }

    public String getIdLang() {
        return idLang;
    }

    public void setIdRootCategory(String idRootCategory) {
        this.idRootCategory = idRootCategory;
    }

    public String getIdRootCategory() {
        return idRootCategory;
    }

    public void setPriceHt(String priceHt) {
        this.priceHt = priceHt;
    }

    public String getPriceHt() {
        return priceHt;
    }

    public void setIdCategoryProduct(String idCategoryProduct) {
        this.idCategoryProduct = idCategoryProduct;
    }

    public String getIdCategoryProduct() {
        return idCategoryProduct;
    }

    public void setIdProductPictureApp(String idProductPictureApp) {
        this.idProductPictureApp = idProductPictureApp;
    }

    public String getIdProductPictureApp() {
        return idProductPictureApp;
    }

    public void setActiveCriteoPicture(boolean activeCriteoPicture) {
        this.activeCriteoPicture = activeCriteoPicture;
    }

    public boolean isActiveCriteoPicture() {
        return activeCriteoPicture;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive() {
        return active;
    }

    public void setOnlyAm(String onlyAm) {
        this.onlyAm = onlyAm;
    }

    public String getOnlyAm() {
        return onlyAm;
    }

    public void setNbrePiecePicture(boolean nbrePiecePicture) {
        this.nbrePiecePicture = nbrePiecePicture;
    }

    public boolean isNbrePiecePicture() {
        return nbrePiecePicture;
    }

    public void setContainsAlcohol(String containsAlcohol) {
        this.containsAlcohol = containsAlcohol;
    }

    public String getContainsAlcohol() {
        return containsAlcohol;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getKcal() {
        return kcal;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setPublicPicture(boolean publicPicture) {
        this.publicPicture = publicPicture;
    }

    public boolean isPublicPicture() {
        return publicPicture;
    }

    public void setActiveVae(String activeVae) {
        this.activeVae = activeVae;
    }

    public String getActiveVae() {
        return activeVae;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setIdTaxe(String idTaxe) {
        this.idTaxe = idTaxe;
    }

    public String getIdTaxe() {
        return idTaxe;
    }

    public void setPriceTtc(String priceTtc) {
        this.priceTtc = priceTtc;
    }

    public String getPriceTtc() {
        return priceTtc;
    }

    public void setKcalPicture(boolean kcalPicture) {
        this.kcalPicture = kcalPicture;
    }

    public boolean isKcalPicture() {
        return kcalPicture;
    }

    public void setNbrePiece(String nbrePiece) {
        this.nbrePiece = nbrePiece;
    }

    public String getNbrePiece() {
        return nbrePiece;
    }

    public void setTitreSeo(String titreSeo) {
        this.titreSeo = titreSeo;
    }

    public String getTitreSeo() {
        return titreSeo;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setActifWebmobilePicture(boolean actifWebmobilePicture) {
        this.actifWebmobilePicture = actifWebmobilePicture;
    }

    public boolean isActifWebmobilePicture() {
        return actifWebmobilePicture;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setActiveSauce(String activeSauce) {
        this.activeSauce = activeSauce;
    }

    public String getActiveSauce() {
        return activeSauce;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescriptionSeo(String descriptionSeo) {
        this.descriptionSeo = descriptionSeo;
    }

    public String getDescriptionSeo() {
        return descriptionSeo;
    }

    public void setJsonMemberPublic(String jsonMemberPublic) {
        this.jsonMemberPublic = jsonMemberPublic;
    }

    public String getJsonMemberPublic() {
        return jsonMemberPublic;
    }

    public void setIdProductLang(String idProductLang) {
        this.idProductLang = idProductLang;
    }

    public String getIdProductLang() {
        return idProductLang;
    }

    public void setIdCategoryDefault(String idCategoryDefault) {
        this.idCategoryDefault = idCategoryDefault;
    }

    public String getIdCategoryDefault() {
        return idCategoryDefault;
    }

    public void setIdProductPicture(String idProductPicture) {
        this.idProductPicture = idProductPicture;
    }

    public String getIdProductPicture() {
        return idProductPicture;
    }

    public void setLinkRewrite(String linkRewrite) {
        this.linkRewrite = linkRewrite;
    }

    public String getLinkRewrite() {
        return linkRewrite;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getNotice() {
        return notice;
    }

    public void setDescriptionSecondary(String descriptionSecondary) {
        this.descriptionSecondary = descriptionSecondary;
    }

    public String getDescriptionSecondary() {
        return descriptionSecondary;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setActiveCrossSelling(int activeCrossSelling) {
        this.activeCrossSelling = activeCrossSelling;
    }

    public int getActiveCrossSelling() {
        return activeCrossSelling;
    }

    public void setCodeVentilation(String codeVentilation) {
        this.codeVentilation = codeVentilation;
    }

    public String getCodeVentilation() {
        return codeVentilation;
    }

    public void setDateAdd(String dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getDateAdd() {
        return dateAdd;
    }

    public void setActifWebmobile(String actifWebmobile) {
        this.actifWebmobile = actifWebmobile;
    }

    public String getActifWebmobile() {
        return actifWebmobile;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setIdProductPictureMarketing(boolean idProductPictureMarketing) {
        this.idProductPictureMarketing = idProductPictureMarketing;
    }

    public boolean isIdProductPictureMarketing() {
        return idProductPictureMarketing;
    }

    public void setDateUpd(String dateUpd) {
        this.dateUpd = dateUpd;
    }

    public String getDateUpd() {
        return dateUpd;
    }

    public void setActiveUpSelling(int activeUpSelling) {
        this.activeUpSelling = activeUpSelling;
    }

    public int getActiveUpSelling() {
        return activeUpSelling;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getItemClickCount() {
        return itemClickCount;
    }

    public void setItemClickCount(int itemClickCount) {
        this.itemClickCount = itemClickCount;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    @Override
    public String toString() {
        return
                "ProductsItem{" +
                        "active_sauce_picture = '" + activeSaucePicture + '\'' +
                        ",category_name = '" + categoryName + '\'' +
                        ",picture_url = '" + pictureUrl + '\'' +
                        ",only_bynight = '" + onlyBynight + '\'' +
                        ",footer_seo = '" + footerSeo + '\'' +
                        ",html_name = '" + htmlName + '\'' +
                        ",active_delivery = '" + activeDelivery + '\'' +
                        ",active_criteo = '" + activeCriteo + '\'' +
                        ",reference = '" + reference + '\'' +
                        ",id_lang = '" + idLang + '\'' +
                        ",id_root_category = '" + idRootCategory + '\'' +
                        ",price_ht = '" + priceHt + '\'' +
                        ",id_category_product = '" + idCategoryProduct + '\'' +
                        ",id_product_picture_app = '" + idProductPictureApp + '\'' +
                        ",active_criteo_picture = '" + activeCriteoPicture + '\'' +
                        ",active = '" + active + '\'' +
                        ",only_am = '" + onlyAm + '\'' +
                        ",nbre_piece_picture = '" + nbrePiecePicture + '\'' +
                        ",contains_alcohol = '" + containsAlcohol + '\'' +
                        ",kcal = '" + kcal + '\'' +
                        ",id_product = '" + idProduct + '\'' +
                        ",public_picture = '" + publicPicture + '\'' +
                        ",active_vae = '" + activeVae + '\'' +
                        ",name = '" + name + '\'' +
                        ",position = '" + position + '\'' +
                        ",id_taxe = '" + idTaxe + '\'' +
                        ",price_ttc = '" + priceTtc + '\'' +
                        ",kcal_picture = '" + kcalPicture + '\'' +
                        ",nbre_piece = '" + nbrePiece + '\'' +
                        ",titre_seo = '" + titreSeo + '\'' +
                        ",id_category = '" + idCategory + '\'' +
                        ",actif_webmobile_picture = '" + actifWebmobilePicture + '\'' +
                        ",link = '" + link + '\'' +
                        ",active_sauce = '" + activeSauce + '\'' +
                        ",description = '" + description + '\'' +
                        ",description_seo = '" + descriptionSeo + '\'' +
                        ",public = '" + jsonMemberPublic + '\'' +
                        ",id_product_lang = '" + idProductLang + '\'' +
                        ",id_category_default = '" + idCategoryDefault + '\'' +
                        ",id_product_picture = '" + idProductPicture + '\'' +
                        ",link_rewrite = '" + linkRewrite + '\'' +
                        ",notice = '" + notice + '\'' +
                        ",description_secondary = '" + descriptionSecondary + '\'' +
                        ",cover_url = '" + coverUrl + '\'' +
                        ",active_cross_selling = '" + activeCrossSelling + '\'' +
                        ",code_ventilation = '" + codeVentilation + '\'' +
                        ",date_add = '" + dateAdd + '\'' +
                        ",actif_webmobile = '" + actifWebmobile + '\'' +
                        ",description_short = '" + descriptionShort + '\'' +
                        ",id_product_picture_marketing = '" + idProductPictureMarketing + '\'' +
                        ",date_upd = '" + dateUpd + '\'' +
                        ",active_up_selling = '" + activeUpSelling + '\'' +
                        "}";
    }
}