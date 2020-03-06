package com.ats.shivshambhoo.interfaces;

import com.ats.shivshambhoo.model.AddProject;
import com.ats.shivshambhoo.model.AddProjectModel;
import com.ats.shivshambhoo.model.Cust;
import com.ats.shivshambhoo.model.CustomerType;
import com.ats.shivshambhoo.model.DetailPoModel;
import com.ats.shivshambhoo.model.Document;
import com.ats.shivshambhoo.model.DocumentHeader;
import com.ats.shivshambhoo.model.EnquiryHeader;
import com.ats.shivshambhoo.model.EnquiryHeaderDisp;
import com.ats.shivshambhoo.model.EnquirySource;
import com.ats.shivshambhoo.model.GetCust;
import com.ats.shivshambhoo.model.GetItemTax;
import com.ats.shivshambhoo.model.GetOrder;
import com.ats.shivshambhoo.model.GetOrderDetail;
import com.ats.shivshambhoo.model.Info;
import com.ats.shivshambhoo.model.ItemsByPlant;
import com.ats.shivshambhoo.model.LoginResponse;
import com.ats.shivshambhoo.model.OrderHeader;
import com.ats.shivshambhoo.model.POHeader;
import com.ats.shivshambhoo.model.POItems;
import com.ats.shivshambhoo.model.PaymentTerm;
import com.ats.shivshambhoo.model.PdfModel;
import com.ats.shivshambhoo.model.Plant;
import com.ats.shivshambhoo.model.PlantDetail;
import com.ats.shivshambhoo.model.PoHeaderModel;
import com.ats.shivshambhoo.model.PoListModel;
import com.ats.shivshambhoo.model.Project;
import com.ats.shivshambhoo.model.QuotationHeader;
import com.ats.shivshambhoo.model.QuotationHeaderDisp;
import com.ats.shivshambhoo.model.QuotationHeaderForPdf;
import com.ats.shivshambhoo.model.Status;
import com.ats.shivshambhoo.model.TermHeader;
import com.ats.shivshambhoo.model.Uom;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InterfaceApi {

    @POST("loginUser")
    Call<LoginResponse> doLogin(@Query("usrMob") String usrMob, @Query("userPass") String userPass);

    @GET("getAllCompanyPlantList")
    Call<ArrayList<Plant>> getPlantList();

    @GET("getAllCustTypeList")
    Call<ArrayList<CustomerType>> getCustomerTypeList();

    @POST("saveCust")
    Call<Cust> saveCustomer(@Body Cust cust);


    @POST("getCustListByPlant")
    Call<ArrayList<Cust>> getCustomerList(@Query("plantId") int plantId);

    @POST("getPlantByPlantId")
    Call<PlantDetail> getPlantByPlantId(@Query("plantId") int plantId);

    @POST("getGetItemsByPlantId")
    Call<ArrayList<ItemsByPlant>> getItemsByPlant(@Query("plantId") int plantId);

    @GET("getAllUomList")
    Call<ArrayList<Uom>> getUOMList();

    @POST("getAllProList")
    Call<ArrayList<AddProject>> getAllProList(@Query("plantId") int plantId);

    @POST("saveProject")
    Call<AddProjectModel> saveProject(@Body AddProjectModel addProjectModel);

    @POST("saveEnqHeaderAndDetail")
    Call<EnquiryHeader> saveEnquiry(@Body EnquiryHeader enquiryHeader);

    @POST("getQuotPrintData")
    Call<ArrayList<PdfModel>> getPdfData(@Query("quotIdList") int quotIdList);


    @POST("savePurchaseOrder")
    Call<PoHeaderModel> savePurchaseOrder(@Body PoHeaderModel poHeaderModel);


    @POST("getDocument")
    Call<Document> getDocument(@Query("docCode") int docCode);

    @POST("updateQuatationStatus")
    Call<Status> updateQuatationStatus(@Query("quotHeadId") int quotHeadId);

    @POST("updateDocSrNo")
    Call<Info> updateDocument(@Query("docCode") int docCode, @Query("srNo") int srNo);

    @POST("updateEnqCounter")
    Call<Info> updateEnqCounter(@Query("plantId") int plantId, @Query("enqCount") int enqCount);

    @POST("updateQuotCounter")
    Call<Info> updateQuotCounter(@Query("plantId") int plantId, @Query("quotCount") int enqCount);

    @POST("updateOrderCounter")
    Call<Info> updateOrderCounter(@Query("plantId") int plantId, @Query("ordCount") int enqCount);

    @POST("updateEnquiry")
    Call<Info> updateEnquiryRemark(@Query("enqHeadId") int enqHeadId, @Query("enqHRemark") String enqHRemark, @Query("enqDate") String enqDate, @Query("enqGenId") int enqGenId, @Query("enqPriority") int enqPriority);

    @GET("getEnqHeaderList")
    Call<ArrayList<EnquiryHeader>> getEnquiryList();

    @GET("getAllEGFList")
    Call<ArrayList<EnquirySource>> getEnquirySourceList();

    @POST("saveQuotHeaderAndDetail")
    Call<QuotationHeader> saveQuotation(@Body QuotationHeader quotationHeader);

    @POST("getEnqListByPlantIdAndCustIdForApp")
    Call<ArrayList<EnquiryHeaderDisp>> getEnquiryListByPlant(@Query("plantId") int plantId, @Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("custId") int custId);

    @POST("getQuotListByPlantIdAndCustId")
    Call<ArrayList<QuotationHeaderDisp>> getQuotationListByPlant(@Query("plantId") int plantId, @Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("custId") int custId);

    @POST("getProjectByCustId")
    Call<ArrayList<Project>> getProjectByCustomer(@Query("custId") int custId);

    @POST("getAllItemTaxList")
    Call<ArrayList<GetItemTax>> getItemsTax(@Query("plantId") int plantId);

    @POST("getDocHeaderByDocIdAndPlantId")
    Call<ArrayList<DocumentHeader>> getDocumentTerm(@Query("docId") int docId,@Query("plantId") int plantId);

    @GET("getAllPaymentTermList")
    Call<ArrayList<PaymentTerm>> getPaymentTerm();

    @POST("updateQuotation")
    Call<Info> updateQuotation(@Query("quotHeadId") int quotHeadId, @Query("payTermId") int payTermId, @Query("quotDate") String quotDate, @Query("otherRemark1") String otherRemark1, @Query("projId") int projId, @Query("quotTermId") int quotTermId);

    @POST("getPoHeaderByCustIdAndStatus")
    Call<ArrayList<POHeader>> getPOListByCustId(@Query("custId") int custId, @Query("statusList") ArrayList<Integer> statusList);

    @POST("getPoDetailForOrderByPoId")
    Call<ArrayList<POItems>> getPOItemsByPOId(@Query("poId") int custId);

    @POST("saveOrder")
    Call<OrderHeader> saveOrder(@Body OrderHeader orderHeader);

//    @POST("saveOrder")
//    Call<GetOrder> saveOrder(@Body GetOrder getOrder);

    @POST("getOrderListBetDate")
    Call<ArrayList<GetOrder>> getOrderList(@Query("plantId") int plantId, @Query("custId") int custId, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("deleteCust")
    Call<Info> deleteCustomer(@Query("custId") int custId);

    @POST("deleteQuotHeader")
    Call<Info> deleteQuotation(@Query("quotHeadId") int quotHeadId);

    @POST("deleteProject")
    Call<Info> deleteProject(@Query("projId") int projId);

    @POST("saveUniqueCustomer")
    Call<Info> checkCustomerExists(@Query("custMobNo") String custMobNo);

    @POST("getQuotListByPlantIdAndCustIdStatus")
    Call<ArrayList<QuotationHeaderDisp>> getAllQuotationListByPlant(@Query("plantId") int plantId, @Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("custId") int custId, @Query("status") int status);

    @POST("getQuotListByPlantIdAndCustIdStatusList")
    Call<ArrayList<QuotationHeaderDisp>> getAllQuotationListByPlantList(@Query("plantId") int plantId, @Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("custId") int custId, @Query("status") List<Integer> status);

    @POST("getPoListByDateAndStatus")
    Call<ArrayList<PoListModel>> getAllPoListByPlant(@Query("plantId") int plantId, @Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("custId") int custId, @Query("statusList") int status);

    @POST("getPoHeaderWithDetail")
    Call<DetailPoModel> getAllPoDetailListByPlant(@Query("poId") int poId);

    @POST("getOrderDetailList")
    Call<ArrayList<GetOrderDetail>> getOrderDetailList(@Query("orderHeaderId") int orderHeaderId);

    @POST("getQuotListByPlantIdAndCustIdAndStatusList")
    Call<ArrayList<QuotationHeaderDisp>> getQuotationListByPlantAndStatus(@Query("plantId") int plantId, @Query("custId") int custId, @Query("statusList") ArrayList<String> statusList);

    @POST("getCustomerByCustId")
    Call<GetCust> getCustomerById(@Query("custId") int custId);

    @POST("getQuotHeaderWithNameByQuotHeadId")
    Call<QuotationHeaderForPdf> getQuotationById(@Query("quotHeadId") int quotHeadId);

    @POST("getCustomerByCustId")
    Call<TermHeader> getTermData(@Query("termId") int termId);

}
