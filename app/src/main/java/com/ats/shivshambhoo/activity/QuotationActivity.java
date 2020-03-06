package com.ats.shivshambhoo.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.shivshambhoo.BuildConfig;
import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.model.DocTermDetail;
import com.ats.shivshambhoo.model.DocumentHeader;
import com.ats.shivshambhoo.model.GetCust;
import com.ats.shivshambhoo.model.Info;
import com.ats.shivshambhoo.model.PaymentTerm;
import com.ats.shivshambhoo.model.PdfModel;
import com.ats.shivshambhoo.model.Plant;
import com.ats.shivshambhoo.model.Project;
import com.ats.shivshambhoo.model.QuotationHeader;
import com.ats.shivshambhoo.model.QuotationHeaderForPdf;
import com.ats.shivshambhoo.util.CommonDialog;
import com.ats.shivshambhoo.util.CustomSharedPreference;
import com.ats.shivshambhoo.util.PermissionsUtil;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuotationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCustName, tvQuoteNo, tvDate, tvPlantName;
    private Spinner spProject, spQuoteTerm, spPaymentTerm;
    private Button btnSubmit;
    private EditText edRemark, edDate;

    Plant plant;

    ArrayList<String> projectNameList = new ArrayList<>();
    ArrayList<Integer> projectIdList = new ArrayList<>();
    private ArrayList<PdfModel> pdfList = new ArrayList<>();

    long dateMillis;
    int yyyy, mm, dd;

    ArrayList<DocumentHeader> quoteTermList = new ArrayList<>();
    ArrayList<String> quoteTermNameList = new ArrayList<>();
    ArrayList<Integer> quoteTermIdList = new ArrayList<>();
    ArrayList<DocTermDetail> quoteTermDetailList = new ArrayList<>();

    ArrayList<String> paymentTermNameList = new ArrayList<>();
    ArrayList<Integer> paymentTermIdList = new ArrayList<>();

    QuotationHeader quotationHeader;
    QuotationHeaderForPdf quoteData;
    PdfModel pdfModel;

    int allQuote = 0, emailQuote = 0, quotStatus = 0;

    //------PDF------
    private PdfPCell cell;
    private String path;
    private File dir;
    private File file;
    private TextInputLayout inputLayoutBillTo, inputLayoutEmailTo;
    double totalAmount = 0;
    int day, month, year;
    long dateInMillis;
    long amtLong;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#ffffff");
    BaseColor myColor1 = WebColors.getRGBColor("#cbccce");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        setTitle("Quotation");

        tvPlantName = findViewById(R.id.tvPlantName);
        tvCustName = findViewById(R.id.tvCustName);
        tvQuoteNo = findViewById(R.id.tvQuoteNo);
        tvDate = findViewById(R.id.tvDate);
        spProject = findViewById(R.id.spProject);
        spQuoteTerm = findViewById(R.id.spQuoteTerm);
        spPaymentTerm = findViewById(R.id.spPaymentTerm);
        btnSubmit = findViewById(R.id.btnSubmit);
        edRemark = findViewById(R.id.edRemark);
        edDate = findViewById(R.id.edDate);

        btnSubmit.setOnClickListener(this);
        edDate.setOnClickListener(this);

        if (PermissionsUtil.checkAndRequestPermissions(this)) {
        }

        try {
            String plantStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_PLANT);
            Gson gsonPlant = new Gson();
            plant = gsonPlant.fromJson(plantStr, Plant.class);
            if (plant != null) {

                tvPlantName.setText("" + plant.getPlantName());

            }

        } catch (Exception e) {
        }

        int plantId= plant.getPlantId();
        getPaymentTermList();
        getQuotationTerm(plantId);

        allQuote = getIntent().getIntExtra("allQuote", 0);
        emailQuote = getIntent().getIntExtra("email", 0);
        quotStatus = getIntent().getIntExtra("status", 0);

        if (emailQuote == 1) {
            btnSubmit.setText("Submit and Email");
        } else {
            btnSubmit.setText("Submit");
        }

        String custName = getIntent().getStringExtra("customer");
        String quoteStr = getIntent().getStringExtra("model");
        Gson gson = new Gson();
        quotationHeader = gson.fromJson(quoteStr, QuotationHeader.class);

        if (quotationHeader != null) {

            getProjectList();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            tvCustName.setText("" + custName);
            edDate.setText("" + sdf.format(System.currentTimeMillis()));
            tvDate.setText("" + sdf1.format(System.currentTimeMillis()));
            tvQuoteNo.setText("" + quotationHeader.getQuotNo());

        }

        dir = new File(Environment.getExternalStorageDirectory() + File.separator, "ShivShambhu" + File.separator + "Quotation");
        if (!dir.exists()) {
            dir.mkdirs();
        }


        //getQuotationById(401);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edDate) {

            int yr, mn, dy;
            if (dateMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(dateMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog = new DatePickerDialog(QuotationActivity.this, dateListener, yr, mn, dy);
            dialog.show();

        } else if (view.getId() == R.id.btnSubmit) {

            String remark = edRemark.getText().toString().trim();

            String date = tvDate.getText().toString();

            int projectId = 0;
            try {
                projectId = projectIdList.get(spProject.getSelectedItemPosition());
            } catch (Exception e) {
            }

            int quoteTermId = 0;
            try {
                quoteTermId = quoteTermIdList.get(spQuoteTerm.getSelectedItemPosition());
            } catch (Exception e) {
            }

            int payTermId = 0;
            try {
                payTermId = paymentTermIdList.get(spPaymentTerm.getSelectedItemPosition());
            } catch (Exception e) {
            }



            if (date.isEmpty()) {
                edDate.setError("Required");
            } else if (projectId == 0) {
                edDate.setError(null);
                Toast.makeText(this, "Please select project", Toast.LENGTH_SHORT).show();
            } else if (quoteTermId == 0) {
                Toast.makeText(this, "Please select quotation term", Toast.LENGTH_SHORT).show();
            } else if (payTermId == 0) {
                Toast.makeText(this, "Please select payment term", Toast.LENGTH_SHORT).show();
            }
//            else if (remark.isEmpty()) {
//                edRemark.setError("Required");
            //}
            else {
//                edRemark.setError(null);

                updateQuotation(quotationHeader.getQuotHeadId(), payTermId, date, remark, projectId, quoteTermId);
            }


        }

    }

    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDate.setText(dd + "-" + mm + "-" + yyyy);
            tvDate.setText(yyyy + "-" + mm + "-" + dd);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            dateMillis = calendar.getTimeInMillis();
        }
    };


    public void getProjectList() {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Project>> listCall = Constants.myInterface.getProjectByCustomer(quotationHeader.getCustId());
            listCall.enqueue(new Callback<ArrayList<Project>>() {
                @Override
                public void onResponse(Call<ArrayList<Project>> call, Response<ArrayList<Project>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PROJECT LIST : ", " - " + response.body());

                            projectNameList.clear();
                            projectIdList.clear();
                           // projectNameList.add("Select");

                            if (response.body().size() > 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    projectIdList.add(response.body().get(i).getProjId());
                                    projectNameList.add(response.body().get(i).getProjName());
                                }

                                ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(QuotationActivity.this, android.R.layout.simple_spinner_dropdown_item, projectNameList);
                                spProject.setAdapter(projectAdapter);

                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Project>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void getQuotationTerm(int plantId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DocumentHeader>> listCall = Constants.myInterface.getDocumentTerm(2,plantId);
            listCall.enqueue(new Callback<ArrayList<DocumentHeader>>() {
                @Override
                public void onResponse(Call<ArrayList<DocumentHeader>> call, Response<ArrayList<DocumentHeader>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("QUOTE TERM LIST : ", " - " + response.body());

                            quoteTermList.clear();
                            quoteTermIdList.clear();
                            quoteTermNameList.clear();
                            quoteTermDetailList.clear();
                            quoteTermNameList.add("Select");
                            quoteTermIdList.add(0);
                            quoteTermList = response.body();
                            if (quoteTermList.size() > 0) {
                                for (int i = 0; i < quoteTermList.size(); i++) {
                                    quoteTermNameList.add(quoteTermList.get(i).getTermTitle());
                                    quoteTermIdList.add(quoteTermList.get(i).getTermId());
                                    //quoteTermDetailList.add(quoteTermList.get(i).getDetailList());
                                }


                                ArrayAdapter<String> quoteTermAdapter = new ArrayAdapter<>(QuotationActivity.this, android.R.layout.simple_spinner_dropdown_item, quoteTermNameList);
                                spQuoteTerm.setAdapter(quoteTermAdapter);

                                if(quotationHeader!=null)
                                {
                                    int pos=0;
                                    for(int i=0;i<quoteTermIdList.size();i++)
                                    {
                                        if(quoteTermIdList.get(i)==quotationHeader.getQuotTermId())
                                        {
                                            pos=i;
                                            Log.e("PayTerm","----------------"+quotationHeader.getQuotTermId());
                                            break;
                                        }
                                    }
                                    spQuoteTerm.setSelection(pos);
                                }
                            }


                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DocumentHeader>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPaymentTermList() {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<PaymentTerm>> listCall = Constants.myInterface.getPaymentTerm();
            listCall.enqueue(new Callback<ArrayList<PaymentTerm>>() {
                @Override
                public void onResponse(Call<ArrayList<PaymentTerm>> call, Response<ArrayList<PaymentTerm>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PAYMENT TERM LIST : ", " - " + response.body());

                            paymentTermIdList.clear();
                            paymentTermNameList.clear();
                            paymentTermNameList.add("Select");
                            paymentTermIdList.add(0);
                            if (response.body().size() > 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    paymentTermIdList.add(response.body().get(i).getPayTermId());
                                    paymentTermNameList.add(response.body().get(i).getPayTerm());
                                }

                                ArrayAdapter<String> payTermAdapter = new ArrayAdapter<>(QuotationActivity.this, android.R.layout.simple_spinner_dropdown_item, paymentTermNameList);
                                spPaymentTerm.setAdapter(payTermAdapter);

                                Log.e("Quotation Header","------------------"+quotationHeader);
                                if(quotationHeader!=null)
                                {
                                    int pos=0;
                                    for(int i=0;i<paymentTermIdList.size();i++)
                                    {
                                        if(quotationHeader.getPayTermId()==paymentTermIdList.get(i))
                                        {
                                            pos=i;
                                            Log.e("PayTerm","----------------"+quotationHeader.getPayTermId());
                                            break;
                                        }
                                    }
                                    spPaymentTerm.setSelection(pos);
                                }

                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<PaymentTerm>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateQuotation(final int quoteId, int payTermId, String date, String remark, int projectId, int quoteTermId) {

        Log.e("PARAMETERS : ", "        QUOTATION : " + quoteTermId + "        PAYTERM : " + payTermId);
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateQuotation(quoteId, payTermId, date, remark, projectId, quoteTermId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE QUOTATION: ", " - " + response.body());

                            if (!response.body().isError()) {
                                Toast.makeText(QuotationActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                                ft.replace(R.id.content_frame, new CustomerListFragment(), "HomeFragment");
//                                ft.commit();

                                if (allQuote == 0) {
                                    Intent intent = new Intent(QuotationActivity.this, HomeActivity.class);
                                    intent.putExtra("type", 2);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                } else if (allQuote == 1) {

                                    Intent intent = new Intent(QuotationActivity.this, HomeActivity.class);
                                    intent.putExtra("type", 3);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }

                                //getQuotationById(quoteId);
                                PdfData(quoteId);
                                // shareViaEmail(quotationHeader.getCustId());


                            } else {
                                Toast.makeText(QuotationActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(QuotationActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void PdfData(int quoteId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<PdfModel>> listCall = Constants.myInterface.getPdfData(quoteId);
            listCall.enqueue(new Callback<ArrayList<PdfModel>>() {
                @Override
                public void onResponse(Call<ArrayList<PdfModel>> call, Response<ArrayList<PdfModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PDf List : ", " - " + response.body().toString());
                            pdfList = response.body();
                            Log.e("PDf List Model: ", " - " + pdfList);
                            if(pdfList != null)
                            {
                                pdfModel=pdfList.get(0);

//                                if (pdfModel.getQuotDetPrint() != null) {
//                                    ArrayList<QuotDetPrint> detailList = new ArrayList<>();
//                                    for (int i = 0; i < pdfModel.getQuotDetPrint().size(); i++) {
//                                        detailList.add(pdfModel.getQuotDetPrint().get(i));
//                                    }

//                                }


                                createQuotationPDF(pdfModel);



                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null pdf : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("ExceptionPdf : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<PdfModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure pdf: ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void createQuotationPDF(PdfModel quoteHeader) {

        final CommonDialog commonDialog = new CommonDialog(getApplication(), "Loading", "Please Wait...");
        commonDialog.show();

        Log.e("fileNmae","-------------------------------fileName--------------------"+quoteHeader.getQuotDetPrint().get(0).getQuotHeadId());


        Document doc = new Document();

        doc.setMargins(-16, -17, 10, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font boldFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldFont1WithUnderline = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD | Font.UNDERLINE);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        Font textFontWithUnderline = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL | Font.UNDERLINE);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();


            String fileName = quoteHeader.getQuotDetPrint().get(0).getQuotHeadId() + "_Quotation.pdf";


            file = new File(dir, fileName);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();


            try {

                //create table
                PdfPTable pt = new PdfPTable(3);
                float[] colWidth1 = new float[]{10,30, 30};
                pt.setWidths(colWidth1);
                pt.setTotalWidth(colWidth1);
                pt.setWidthPercentage(100);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);


                cell = new PdfPCell(new Paragraph(quoteHeader.getComp().getCompName(), boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                pt.addCell(cell);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.pdf_icon);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50 , stream);
                Image myImg = Image.getInstance(stream.toByteArray());
                myImg.setAlignment(Image.LEFT);
                myImg.scaleAbsolute(60,60);
                pt.setHorizontalAlignment(0);
                //pt.addCell(new PdfPCell(myImg).setBorder(Rectangle.NO_BORDER));

                cell = new PdfPCell(myImg);
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph( quoteHeader.getComp().getCompOfficeAdd() +
                        "\n"+ "Mobile No." + quoteHeader.getCust().getCustMobNo() + "\n"+
                        "Email:"+quoteHeader.getCust().getCustEmail(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(3);
                pt.addCell(cell);

                PdfPTable pdf = new PdfPTable(1);
                pdf.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("QUOTATION", boldTextFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);

                cell = new PdfPCell(new Paragraph("", boldTextFont));
                cell.setBorder(Rectangle.BOTTOM);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);


                //---------------------------------------------------------------


                PdfPTable ptDate = new PdfPTable(4);
                float[] colWidth = new float[]{17, 50, 50, 15};
                ptDate.setWidths(colWidth);
                ptDate.setTotalWidth(colWidth);
                ptDate.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Quotation No: ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptDate.addCell(cell);

                String quotNumber=quoteHeader.getQuotDetPrint().get(0).getQuotNo();
                String number= quotNumber.substring(5,quotNumber.length());

                cell = new PdfPCell(new Paragraph("" +number));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                cell.setColspan(1);
                ptDate.addCell(cell);

                cell = new PdfPCell(new Paragraph("Date: ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                cell.setColspan(1);
                ptDate.addCell(cell);

                if(quoteHeader.getQuotDetPrint().size()>0) {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(quoteHeader.getQuotDetPrint().get(0).getQuotDate());

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                    cell = new PdfPCell(new Paragraph("" + sdf1.format(date.getTime()), boldFont1));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(2);
                    cell.setColspan(1);
                    ptDate.addCell(cell);
                }

                //------------------------------------------------------------------

                PdfPTable ptTo = new PdfPTable(1);
                ptTo.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptTo.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);


                cell = new PdfPCell(new Paragraph("To", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + quoteHeader.getCust().getCustName(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);


                //-------------------------------------------------------------------

                PdfPTable ptProject = new PdfPTable(2);
                float[] colProjWidth = new float[]{27, 50};
                ptProject.setWidths(colProjWidth);
                ptProject.setTotalWidth(colProjWidth);
                ptProject.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Material Rate List With Carting Only For", textFontWithUnderline));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + quoteHeader.getProj().getProjName() + " site", boldFont1WithUnderline));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject.addCell(cell);


                //--------------------------------------------------------------------

                PdfPTable ptTo1 = new PdfPTable(1);
                ptTo1.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Dear Sir,", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo1.addCell(cell);


                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo1.addCell(cell);

                cell = new PdfPCell(new Paragraph("This has a reference to your site telephonic discussion with you regarding the supply of " +quoteHeader.getQuotDetPrint().get(0).getPlantName()+ " to\n" +
                        "your site In this connection we are submitting herewith our competitive offer for the supply\n" +
                        "of " +quoteHeader.getQuotDetPrint().get(0).getPlantName()+" on the following terms and conditions.", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo1.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo1.addCell(cell);

                //----------------------------------------------------------------------

                PdfPTable ptItemHead = new PdfPTable(7);
                float[] colItemHeadWidth = new float[]{10, 30, 15, 15, 20, 15, 25};
                ptItemHead.setWidths(colItemHeadWidth);
                ptItemHead.setTotalWidth(colItemHeadWidth);
                ptItemHead.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Sr No", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Material", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Unit", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Tax %", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Taxable", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Tax", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Rate / Brass", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                //-------------------------------------------------------------------------------

             /*   PdfPTable ptItem = new PdfPTable(7);
                float[] colItemWidth = new float[]{10, 30, 15, 15, 20, 15, 25};
                ptItem.setWidths(colItemWidth);
                ptItem.setTotalWidth(colItemWidth);
                ptItem.setWidthPercentage(100);*/

                if (quoteHeader.getQuotDetPrint().size() > 0) {

                    for (int i = 0; i < quoteHeader.getQuotDetPrint().size(); i++) {

                        cell = new PdfPCell(new Paragraph("" + (i + 1), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + quoteHeader.getQuotDetPrint().get(i).getItemName(), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + quoteHeader.getQuotDetPrint().get(i).getUomName(), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        float tax = (quoteHeader.getQuotDetPrint().get(i).getTaxPer());

                        cell = new PdfPCell(new Paragraph("" + tax, textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + quoteHeader.getQuotDetPrint().get(i).getTaxableValue(), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + quoteHeader.getQuotDetPrint().get(i).getTaxValue(), textFont));
                        //  cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + quoteHeader.getQuotDetPrint().get(i).getTotal(), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                    }
                }
                //----------------------------------------------------------------------------------

                PdfPTable ptProject2 = new PdfPTable(2);
                float[] colProjWidth2 = new float[]{5,50};
                ptProject2.setWidths(colProjWidth2);
                ptProject2.setTotalWidth(colProjWidth2);
                ptProject2.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject2.addCell(cell);


                cell = new PdfPCell(new Paragraph("Note :", boldTextFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("1)Above rates are valid till One month.\n", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("2) Cement grade:Ultratech OPC 53Grade.\n", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("3) Our Bank Details –"+quoteHeader.getComp().getCompName()+ "\n" +quoteHeader.getBank().getBankName()+ "\n" +"A/c. No." +quoteHeader.getBank().getAccNo(), boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("4)Toll charges will be paid by you at an actual.\n", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("5) Pumping charges will be extra Rs.175/-\n", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("6)  OUR GST NO.27ADHFS8127N1ZE\n", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);




                //--------------------------------------------------------------------------

                PdfPTable ptTerms = new PdfPTable(1);
                ptTerms.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);

                cell = new PdfPCell(new Paragraph("Notes:- Terms And Conditions", boldTextFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);


                if (quoteHeader.getDocTermList().size() > 0) {

                    for (int i = 0; i < quoteHeader.getDocTermList().size(); i++) {
                        cell = new PdfPCell(new Paragraph("" + (i + 1) + ") " + quoteHeader.getDocTermList().get(i).getTermDesc(), textFont));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptTerms.addCell(cell);
                    }


                }


                //----------------------------------------------------------------------

                PdfPTable ptBank = new PdfPTable(1);
                ptBank.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph("We thank you for considering us for the supply of " +quoteHeader.getQuotDetPrint().get(0).getPlantName()+ " and we assure you of our Best Quality and\n" +
                        "services at all times.", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);
                //-------------------------------------------------------------------------------

                PdfPTable ptProject1 = new PdfPTable(2);
                float[] colProjWidth1 = new float[]{5,50};
                ptProject1.setWidths(colProjWidth1);
                ptProject1.setTotalWidth(colProjWidth1);
                ptProject1.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Note :", boldTextFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject1.addCell(cell);

//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph("I under sign read the above terms and conditions carefully and agree for the same.", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject1.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setHorizontalAlignment(0);
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject1.addCell(cell);


                //-----------------------------------------------------------------------------

                PdfPTable ptThank = new PdfPTable(1);
                ptThank.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptThank.addCell(cell);

                cell = new PdfPCell(new Paragraph( "Thanking you,\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptThank.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptThank.addCell(cell);


                cell = new PdfPCell(new Paragraph( "Yours Faithfully,\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptThank.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptThank.addCell(cell);


//---------------------------------------------------------------------------------


                PdfPTable ptProj = new PdfPTable(2);
                float[] colProjW = new float[]{20,50};
                ptProj.setWidths(colProjW);
                ptProj.setTotalWidth(colProjW);
                ptProj.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("For Shiv Shambhu Buildcon :", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProj.addCell(cell);


                cell = new PdfPCell(new Paragraph("Accepted By, ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                //cell.setColspan(1);
                ptProj.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProj.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProj.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProj.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProj.addCell(cell);


                cell = new PdfPCell(new Paragraph("Authorized Signatory  :", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProj.addCell(cell);


                cell = new PdfPCell(new Paragraph("For. Shrinath Construction ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                //cell.setColspan(1);
                ptProj.addCell(cell);


//------------------------------------------------------------------------------

                PdfPTable ptBankDetail = new PdfPTable(1);
                ptBankDetail.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Our Bank Details:", boldTextFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBankDetail.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBankDetail.addCell(cell);

                cell = new PdfPCell(new Paragraph(quoteHeader.getComp().getCompName(), textFont));
                //  "Shri Gajanan Stone Crusher"
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBankDetail.addCell(cell);

                cell = new PdfPCell(new Paragraph("A/c. No." +quoteHeader.getBank().getAccNo(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBankDetail.addCell(cell);

                cell = new PdfPCell(new Paragraph(quoteHeader.getBank().getBankName(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBankDetail.addCell(cell);

                cell = new PdfPCell(new Paragraph("IFSC Code:"+quoteHeader.getBank().getBankIfsc(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBankDetail.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBankDetail.addCell(cell);

                //---------------------------------------------------------------

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pdf);
                pTable.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptDate);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptTo);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptProject);
                pTable.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptTo1);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptItemHead);
                pTable.addCell(cell);

                if(quoteHeader.getQuotDetPrint().get(0).getPlantName().equals("RMC")) {
                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setColspan(1);
                    cell.addElement(ptProject2);
                    pTable.addCell(cell);
                }

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptTerms);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptBank);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptProject1);
                pTable.addCell(cell);

                if(!quoteHeader.getQuotDetPrint().get(0).getPlantName().equals("RMC"))
                {
                    cell = new PdfPCell();
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setColspan(1);
                    cell.addElement(ptBankDetail);
                    pTable.addCell(cell);
                }

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptThank);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptProj);
                // pTable.addCell(cell);

                pTable.addCell(cell);


                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{10, 50, 30, 30, 30, 30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                doc.add(table);

            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);
                Log.e("File","file1-------------------"+file1);

//                if (emailQuote == 0 && quotStatus == 1) {

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getApplication(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getApplication().startActivity(intent);
                } else {
                    commonDialog.dismiss();
                }


                // }
// else {
//                    shareViaEmail(quoteHeader.getCustId(), fileName);
//                    Toast.makeText(context, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
//                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            // Log.e("Mytag","-------------------Error--------------------"+ e.getStackTrace());
            Toast.makeText(getApplication(), "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
        }
    }

//    private void createQuotationPDF1(PdfModel pdfModel) {
//        final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
//        commonDialog.show();
//
//        Document doc = new Document();
//
//        doc.setMargins(-16, -17, 40, 40);
//        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
//        Font boldFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
//        Font boldFont1WithUnderline = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE);
//        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD | Font.UNDERLINE);
//        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
//        Font textFontWithUnderline = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL | Font.UNDERLINE);
//        try {
//
//            Calendar calendar = Calendar.getInstance();
//            day = calendar.get(Calendar.DAY_OF_MONTH);
//            month = calendar.get(Calendar.MONTH) + 1;
//            year = calendar.get(Calendar.YEAR);
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            int minutes = calendar.get(Calendar.MINUTE);
//            dateInMillis = calendar.getTimeInMillis();
//
//            String fileName = pdfModel.getQuotDetPrint().get(0).getQuotHeadId() + "_Quotation.pdf";
//            file = new File(dir, fileName);
//            FileOutputStream fOut = new FileOutputStream(file);
//            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
//
//            Log.d("File Name-------------", "" + file.getName());
//            //open the document
//            doc.open();
//
//
//            // set drawable in cell
//            Drawable myImage =this.getResources().getDrawable(R.drawable.ic_up);
//            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//            try {
//
////                Drawable myImage1=context.getResources().getDrawable(R.mipmap.ic_launcher_foreground);
////                Bitmap bitmap1 = ((BitmapDrawable) myImage1).getBitmap();
////                ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
////                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
////                doc.add((Element) myImage1);
//
////                InputStream ims = context.getAssets().open("ic_launcher_foreground.png");
////                Bitmap bmp = BitmapFactory.decodeStream(ims);
////                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
////                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream2);
////                Image image = Image.getInstance(stream2.toByteArray());
////                doc.add(image);
//
//
//                //create table
//                PdfPTable pt = new PdfPTable(1);
//                pt.setWidthPercentage(100);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(pdfModel.getComp().getCompName(), boldFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(2);
//                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(pdfModel.getComp().getCompOfficeAdd(), textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(2);
//                pt.addCell(cell);
//
////                cell = new PdfPCell(new Paragraph("Statue, Pathardi Phata, Nashik 422010", textFont));
////                cell.setBorder(Rectangle.NO_BORDER);
////                cell.setHorizontalAlignment(2);
////                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("Mobile No." + pdfModel.getCust().getCustMobNo(), textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(2);
//                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("Email:"+pdfModel.getCust().getCustEmail(), textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(2);
//                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(1);
//                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("QUOTATION", boldTextFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(1);
//                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(1);
//                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(1);
//                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("", boldTextFont));
//                cell.setBorder(Rectangle.BOTTOM);
//                cell.setHorizontalAlignment(1);
//                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(1);
//                pt.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(1);
//                pt.addCell(cell);
//
//
//                //---------------------------------------------------------------
//
//
//                PdfPTable ptDate = new PdfPTable(4);
//                float[] colWidth = new float[]{17, 50, 50, 15};
//                ptDate.setWidths(colWidth);
//                ptDate.setTotalWidth(colWidth);
//                ptDate.setWidthPercentage(100);
//
//                cell = new PdfPCell(new Paragraph("Quotation No: ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                //cell.setColspan(1);
//                ptDate.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("" + pdfModel.getQuotDetPrint().get(0).getQuotNo(), boldFont1));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                cell.setColspan(1);
//                ptDate.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("Date: ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(2);
//                cell.setColspan(1);
//                ptDate.addCell(cell);
//
//                if(pdfModel.getQuotDetPrint().size()>0) {
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = sdf.parse(pdfModel.getQuotDetPrint().get(0).getQuotDate());
//
//                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
//
//                    cell = new PdfPCell(new Paragraph("" + sdf1.format(date.getTime()), boldFont1));
//                    cell.setBorder(Rectangle.NO_BORDER);
//                    cell.setHorizontalAlignment(2);
//                    cell.setColspan(1);
//                    ptDate.addCell(cell);
//                }
//
//                //------------------------------------------------------------------
//
//                PdfPTable ptTo = new PdfPTable(1);
//                ptTo.setWidthPercentage(100);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                ptTo.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTo.addCell(cell);
//
//
//                cell = new PdfPCell(new Paragraph("To", boldFont1));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTo.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("" + pdfModel.getCust().getCustName(), textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTo.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTo.addCell(cell);
//
//
//                //-------------------------------------------------------------------
//
//                PdfPTable ptProject = new PdfPTable(2);
//                float[] colProjWidth = new float[]{27, 50};
//                ptProject.setWidths(colProjWidth);
//                ptProject.setTotalWidth(colProjWidth);
//                ptProject.setWidthPercentage(100);
//
//                cell = new PdfPCell(new Paragraph("Material Rate List With Carting Only For", textFontWithUnderline));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptProject.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("" + pdfModel.getProj().getProjName() + " site", boldFont1WithUnderline));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptProject.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptProject.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptProject.addCell(cell);
//
//
//                //--------------------------------------------------------------------
//
//                PdfPTable ptTo1 = new PdfPTable(1);
//                ptTo1.setWidthPercentage(100);
//
//                cell = new PdfPCell(new Paragraph("Dear Sir,", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTo1.addCell(cell);
//
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTo1.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("This has a reference to your site telephonic discussion with you regarding the supply of "+ quoteData.getPlantName()+  "to\n" +
//                        "your site In this connection we are submitting herewith our competitive offer for the supply\n" +
//                        "of "+ quoteData.getPlantName() + "on the following terms and conditions.", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTo1.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTo1.addCell(cell);
//
//                //----------------------------------------------------------------------
//
//                PdfPTable ptItemHead = new PdfPTable(7);
//                float[] colItemHeadWidth = new float[]{10, 30, 15, 15, 20, 15, 25};
//                ptItemHead.setWidths(colItemHeadWidth);
//                ptItemHead.setTotalWidth(colItemHeadWidth);
//                ptItemHead.setWidthPercentage(100);
//
//                cell = new PdfPCell(new Paragraph("Sr No", textFont));
//                cell.setHorizontalAlignment(1);
//                ptItemHead.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("Material", textFont));
//                cell.setHorizontalAlignment(1);
//                ptItemHead.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("Unit", textFont));
//                cell.setHorizontalAlignment(1);
//                ptItemHead.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("Tax %", textFont));
//                cell.setHorizontalAlignment(1);
//                ptItemHead.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("Taxable", textFont));
//                cell.setHorizontalAlignment(1);
//                ptItemHead.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("Tax", textFont));
//                cell.setHorizontalAlignment(1);
//                ptItemHead.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("Rate / Brass", textFont));
//                cell.setHorizontalAlignment(1);
//                ptItemHead.addCell(cell);
//
//                //-------------------------------------------------------------------------------
//
//             /*   PdfPTable ptItem = new PdfPTable(7);
//                float[] colItemWidth = new float[]{10, 30, 15, 15, 20, 15, 25};
//                ptItem.setWidths(colItemWidth);
//                ptItem.setTotalWidth(colItemWidth);
//                ptItem.setWidthPercentage(100);*/
//
//                if (pdfModel.getQuotDetPrint().size() > 0) {
//
//                    for (int i = 0; i < pdfModel.getQuotDetPrint().size(); i++) {
//
//                        cell = new PdfPCell(new Paragraph("" + (i + 1), textFont));
//                        // cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptItemHead.addCell(cell);
//
//                        cell = new PdfPCell(new Paragraph("" + pdfModel.getQuotDetPrint().get(i).getItemName(), textFont));
//                        // cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptItemHead.addCell(cell);
//
//                        cell = new PdfPCell(new Paragraph("" + pdfModel.getQuotDetPrint().get(i).getUomName(), textFont));
//                        // cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptItemHead.addCell(cell);
//
//                        float tax = (pdfModel.getQuotDetPrint().get(i).getTaxPer());
//
//                        cell = new PdfPCell(new Paragraph("" + tax, textFont));
//                        // cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptItemHead.addCell(cell);
//
//                        cell = new PdfPCell(new Paragraph("" + pdfModel.getQuotDetPrint().get(i).getTaxableValue(), textFont));
//                        // cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptItemHead.addCell(cell);
//
//                        cell = new PdfPCell(new Paragraph("" + pdfModel.getQuotDetPrint().get(i).getTaxValue(), textFont));
//                        //  cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptItemHead.addCell(cell);
//
//                        cell = new PdfPCell(new Paragraph("" + pdfModel.getQuotDetPrint().get(i).getTotal(), textFont));
//                        // cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptItemHead.addCell(cell);
//
//                    }
//                }
//
//                //--------------------------------------------------------------------------
//
//                PdfPTable ptTerms = new PdfPTable(1);
//                ptTerms.setWidthPercentage(100);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTerms.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTerms.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("Notes:- Terms And Conditions", boldTextFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTerms.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTerms.addCell(cell);
//
//
//                if (pdfModel.getDocTermList().size() > 0) {
//
//                    for (int i = 0; i < pdfModel.getDocTermList().size(); i++) {
//                        cell = new PdfPCell(new Paragraph("" + (i + 1) + ") " + pdfModel.getDocTermList().get(i).getTermDesc(), textFont));
//                        cell.setBorder(Rectangle.NO_BORDER);
//                        cell.setHorizontalAlignment(0);
//                        ptTerms.addCell(cell);
//                    }
//
//
//                }
//
//
//                //----------------------------------------------------------------------
//
//                PdfPTable ptBank = new PdfPTable(1);
//                ptBank.setWidthPercentage(100);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("We thank you for considering us for the supply of "+ quoteData.getPlantName() +"and we assure you of our Best Quality and\n" +
//                        "services at all times.", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//                //-------------------------------------------------------------------------------
//
//                PdfPTable ptProject1 = new PdfPTable(2);
//                float[] colProjWidth1 = new float[]{27, 50};
//                ptProject1.setWidths(colProjWidth1);
//                ptProject1.setTotalWidth(colProjWidth1);
//                ptProject1.setWidthPercentage(100);
//
//                cell = new PdfPCell(new Paragraph("Note:", boldTextFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                cell.setColspan(1);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("I under sign read the above terms and conditions carefully and agree for the same.", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                cell.setColspan(1);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setHorizontalAlignment(0);
//                cell.setBorder(Rectangle.NO_BORDER);
//                ptBank.addCell(cell);
//
////------------------------------------------------------------------------------
//
//                cell = new PdfPCell(new Paragraph("Our Bank Details:", boldTextFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(pdfModel.getComp().getCompName(), textFont));
//                //  "Shri Gajanan Stone Crusher"
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("A/c. No." +pdfModel.getBank().getAccNo(), textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(pdfModel.getBank().getBankName(), textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("IFSC Code:"+pdfModel.getBank().getBankIfsc(), textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
//
//                //---------------------------------------------------------------
//
//                PdfPTable pTable = new PdfPTable(1);
//                pTable.setWidthPercentage(100);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(1);
//                cell.addElement(pt);
//                pTable.addCell(cell);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(1);
//                cell.addElement(ptDate);
//                pTable.addCell(cell);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(1);
//                cell.addElement(ptTo);
//                pTable.addCell(cell);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(1);
//                cell.addElement(ptProject);
//                pTable.addCell(cell);
//
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(1);
//                cell.addElement(ptTo1);
//                pTable.addCell(cell);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(1);
//                cell.addElement(ptItemHead);
//                pTable.addCell(cell);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(1);
//                cell.addElement(ptTerms);
//                pTable.addCell(cell);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(1);
//                cell.addElement(ptBank);
//
//
//                pTable.addCell(cell);
//
//
//                PdfPTable table = new PdfPTable(6);
//                float[] columnWidth = new float[]{10, 50, 30, 30, 30, 30};
//                table.setWidths(columnWidth);
//                table.setTotalWidth(columnWidth);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setBackgroundColor(myColor);
//                cell.setColspan(6);
//                cell.addElement(pTable);
//
//                table.addCell(cell);//image cell&address
//
//                doc.add(table);
//
//            } finally {
//                doc.close();
//                commonDialog.dismiss();
//
//                File file1 = new File(dir, fileName);
//                Log.e("File","file1-------------------"+file1);
//
////                if (emailQuote == 0 && quotStatus == 1) {
//
//                if (file1.exists()) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
//                    } else {
//                        if (file1.exists()) {
//                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
//                            Uri uri = FileProvider.getUriForFile(this, authorities, file1);
//                            intent.setDataAndType(uri, "application/pdf");
//                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        }
//                    }
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    this.startActivity(intent);
//                } else {
//                    commonDialog.dismiss();
//                }
//
//
//                // }
//// else {
////                    shareViaEmail(quoteHeader.getCustId(), fileName);
////                    Toast.makeText(context, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
////                }
//
//            }
//        } catch (Exception e) {
//            commonDialog.dismiss();
//            e.printStackTrace();
//            // Log.e("Mytag","-------------------Error--------------------"+ e.getStackTrace());
//            Toast.makeText(this, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
//        }
//    }


    public void getQuotationById(int quotId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<QuotationHeaderForPdf> listCall = Constants.myInterface.getQuotationById(quotId);
            listCall.enqueue(new Callback<QuotationHeaderForPdf>() {
                @Override
                public void onResponse(Call<QuotationHeaderForPdf> call, Response<QuotationHeaderForPdf> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("QUOTATION : ", " - " + response.body());

                             quoteData = response.body();

                            if (quoteData != null) {

                                // if (quoteData.getStatus() > 0) {
                                //  getTermsById(quoteData.getQuotTermId(), quoteData);
                                createQuotationPDF1(quoteData);
                                //}
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "---**--------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<QuotationHeaderForPdf> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

   /* public void getTermsById(int termId, final QuotationHeaderForPdf quoteData) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            final TermHeader termHeader = new TermHeader();

            Call<TermHeader> listCall = Constants.myInterface.getTermData(termId);
            listCall.enqueue(new Callback<TermHeader>() {
                @Override
                public void onResponse(Call<TermHeader> call, Response<TermHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("TERM : ", " - " + response.body());

                            TermHeader termData = response.body();

                            createQuotationPDF(quoteData, termData);

                            commonDialog.dismiss();

                        } else {
                            createQuotationPDF(quoteData, termHeader);
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        createQuotationPDF(quoteData, termHeader);
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TermHeader> call, Throwable t) {
                    createQuotationPDF(quoteData, termHeader);
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }*/


    private void shareViaEmail(int custId, final String fileName) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(QuotationActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<GetCust> listCall = Constants.myInterface.getCustomerById(custId);
            listCall.enqueue(new Callback<GetCust>() {
                @Override
                public void onResponse(Call<GetCust> call, Response<GetCust> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CUSTOMER : ", " - " + response.body());

                            if (response.body().getCustId() != 0) {

                                try {
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setType("text/plain");
                                    intent.setData(Uri.parse("mailto:" + response.body().getCustEmail()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Quotation");
                                    intent.putExtra(Intent.EXTRA_TEXT, "Hello Sir,");

                                    File file = new File(Environment.getExternalStorageDirectory() + File.separator, "ShivShambhu" + File.separator + "Quotation" + File.separator + "" + fileName);
                                    if (!file.exists() || !file.canRead()) {
                                        return;
                                    }
                                    Uri uri = Uri.fromFile(file);
                                    intent.putExtra(Intent.EXTRA_STREAM, uri);


                                    startActivity(intent);
                                    Log.e("mail sent----------", "----------------");
                                } catch (Exception e) {
                                    Log.e("error-----------", "is exception raises during sending mail" + e);
                                    e.printStackTrace();
                                }

                            }


                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetCust> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(QuotationActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }


    }

    public void createQuotationPDF1(QuotationHeaderForPdf quoteHeader) {

        final CommonDialog commonDialog = new CommonDialog(QuotationActivity.this, "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();

        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font boldFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldFont1WithUnderline = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD | Font.UNDERLINE);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        Font textFontWithUnderline = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL | Font.UNDERLINE);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = quoteHeader.getQuotHeadId() + "_Quotation.pdf";
            Log.e("fileName","---------------------fileName----------------------"+fileName);
            file = new File(dir, fileName);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();


            //set drawable in cell
            Drawable myImage = QuotationActivity.this.getResources().getDrawable(R.drawable.ic_up);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            try {

                //create table
                PdfPTable pt = new PdfPTable(1);
                pt.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Shri Gajanan Stone Crusher", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Shop No. 4, Swastik Chamber, Opp. Shivaji Maharaj", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Statue, Pathardi Phata, Nashik 422010", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Mobile No. 9822602249", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Email: gajananstone95_nsk@rediffmail.com", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("QUOTATION", boldTextFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("", boldTextFont));
                cell.setBorder(Rectangle.BOTTOM);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);


                //---------------------------------------------------------------


                PdfPTable ptDate = new PdfPTable(4);
                float[] colWidth = new float[]{17, 50, 50, 15};
                ptDate.setWidths(colWidth);
                ptDate.setTotalWidth(colWidth);
                ptDate.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Quotation No: ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptDate.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + quoteHeader.getQuotNo(), boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                cell.setColspan(1);
                ptDate.addCell(cell);

                cell = new PdfPCell(new Paragraph("Date: ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                cell.setColspan(1);
                ptDate.addCell(cell);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(quoteHeader.getQuotDate());

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                cell = new PdfPCell(new Paragraph("" + sdf1.format(date.getTime()), boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                cell.setColspan(1);
                ptDate.addCell(cell);


                //------------------------------------------------------------------

                PdfPTable ptTo = new PdfPTable(1);
                ptTo.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptTo.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);


                cell = new PdfPCell(new Paragraph("To", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + quoteHeader.getCustName(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);


                //-------------------------------------------------------------------

                PdfPTable ptProject = new PdfPTable(2);
                float[] colProjWidth = new float[]{27, 50};
                ptProject.setWidths(colProjWidth);
                ptProject.setTotalWidth(colProjWidth);
                ptProject.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Material Rate List With Carting Only For", textFontWithUnderline));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + quoteHeader.getProjName() + " site", boldFont1WithUnderline));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject.addCell(cell);


                //----------------------------------------------------------------------

                PdfPTable ptItemHead = new PdfPTable(7);
                float[] colItemHeadWidth = new float[]{10, 30, 15, 15, 20, 15, 25};
                ptItemHead.setWidths(colItemHeadWidth);
                ptItemHead.setTotalWidth(colItemHeadWidth);
                ptItemHead.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Sr No", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Material", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Unit", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Tax %", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Taxable", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Tax", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Rate / Brass", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                //-------------------------------------------------------------------------------

             /*   PdfPTable ptItem = new PdfPTable(7);
                float[] colItemWidth = new float[]{10, 30, 15, 15, 20, 15, 25};
                ptItem.setWidths(colItemWidth);
                ptItem.setTotalWidth(colItemWidth);
                ptItem.setWidthPercentage(100);*/

                if (quoteHeader.getGetQuotDetailList().size() > 0) {

                    for (int i = 0; i < quoteHeader.getGetQuotDetailList().size(); i++) {

                        cell = new PdfPCell(new Paragraph("" + (i + 1), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + quoteHeader.getGetQuotDetailList().get(i).getItemName(), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + quoteHeader.getGetQuotDetailList().get(i).getUomName(), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        float tax = (quoteHeader.getGetQuotDetailList().get(i).getCgstPer() + quoteHeader.getGetQuotDetailList().get(i).getSgstPer());

                        cell = new PdfPCell(new Paragraph("" + tax, textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + quoteHeader.getGetQuotDetailList().get(i).getTaxableValue(), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + quoteHeader.getGetQuotDetailList().get(i).getTaxValue(), textFont));
                        //  cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + quoteHeader.getGetQuotDetailList().get(i).getTotal(), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                    }
                }

                //--------------------------------------------------------------------------

                PdfPTable ptTerms = new PdfPTable(1);
                ptTerms.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);

                cell = new PdfPCell(new Paragraph("Notes:- Terms And Conditions", boldTextFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);

                DocumentHeader termHeader = new DocumentHeader();

                if (quoteTermList.size() > 0) {

                    for (int i = 0; i < quoteTermList.size(); i++) {

                        if (quoteHeader.getQuotTermId() == quoteTermList.get(i).getTermId()) {
                            termHeader = quoteTermList.get(i);
                        }

                    }

                    if (termHeader.getDetailList().size() > 0) {

                        for (int i = 0; i < termHeader.getDetailList().size(); i++) {
                            cell = new PdfPCell(new Paragraph("" + (i + 1) + ") " + termHeader.getDetailList().get(i).getTermDesc(), textFont));
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setHorizontalAlignment(0);
                            ptTerms.addCell(cell);
                        }

                    }
                }


                //----------------------------------------------------------------------

                PdfPTable ptBank = new PdfPTable(1);
                ptBank.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph("Our Bank Details:", boldTextFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph("Shri Gajanan Stone Crusher", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph("A/c. No. 913030045779015", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph("Axis Bank Ltd.", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph("IFSC Code: UTIB0000849", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                //---------------------------------------------------------------

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptDate);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptTo);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptProject);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptItemHead);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptTerms);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptBank);
                pTable.addCell(cell);


                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{10, 50, 30, 30, 30, 30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                doc.add(table);

            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);


                if (emailQuote == 0 && quotStatus == 1) {

                    if (file1.exists()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                        } else {
                            if (file1.exists()) {
                                String authorities = BuildConfig.APPLICATION_ID + ".provider";
                                Uri uri = FileProvider.getUriForFile(QuotationActivity.this, authorities, file1);
                                intent.setDataAndType(uri, "application/pdf");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    } else {
                        commonDialog.dismiss();
                    }


                } else {
                    shareViaEmail(quoteHeader.getCustId(), fileName);
                    Toast.makeText(QuotationActivity.this, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(QuotationActivity.this, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
        }
    }


}
