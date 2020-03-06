package com.ats.shivshambhoo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.shivshambhoo.BuildConfig;
import com.ats.shivshambhoo.R;
import com.ats.shivshambhoo.activity.HomeActivity;
import com.ats.shivshambhoo.constants.Constants;
import com.ats.shivshambhoo.fragment.AllQuotationListFragment;
import com.ats.shivshambhoo.fragment.EditQuotationFragment;
import com.ats.shivshambhoo.model.DocTermDetail;
import com.ats.shivshambhoo.model.DocumentHeader;
import com.ats.shivshambhoo.model.GetQuotDetail;
import com.ats.shivshambhoo.model.Info;
import com.ats.shivshambhoo.model.PdfModel;
import com.ats.shivshambhoo.model.Plant;
import com.ats.shivshambhoo.model.PlantDetail;
import com.ats.shivshambhoo.model.QuotationHeaderDisp;
import com.ats.shivshambhoo.util.CommonDialog;
import com.ats.shivshambhoo.util.CustomSharedPreference;
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

public class AllQuotationListAdapter extends RecyclerView.Adapter<AllQuotationListAdapter.MyViewHolder> {

    private ArrayList<QuotationHeaderDisp> quoteList;
    private ArrayList<PlantDetail> plantList = new ArrayList<>();
    private ArrayList<PdfModel> pdfList = new ArrayList<>();
    ArrayList<DocumentHeader> quoteTermList = new ArrayList<>();
    ArrayList<String> quoteTermNameList = new ArrayList<>();
    ArrayList<DocTermDetail> quoteTermDetailList = new ArrayList<>();
    private Context context;
    PlantDetail plantDetail;
    String enqNo ;
    Plant plant;
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



    public AllQuotationListAdapter(ArrayList<QuotationHeaderDisp> quoteList, Context context) {
        this.quoteList = quoteList;
        this.context = context;
        dir = new File(Environment.getExternalStorageDirectory() + File.separator, "ShivShambhu" + File.separator + "Quotation");
        if (!dir.exists()) {
            dir.mkdirs();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCustName, tvQuoteNo, tvDate, tvRemark, tvItems;
        public RecyclerView recyclerView;
        public CardView cardView;
        public ImageView ivEditPending,ivEditQG,ivEditPO,imageView;
        public LinearLayout llItems;

        public MyViewHolder(View view) {
            super(view);
            tvCustName = view.findViewById(R.id.tvCustName);
            tvQuoteNo = view.findViewById(R.id.tvQuoteNo);
            tvDate = view.findViewById(R.id.tvDate);
            tvRemark = view.findViewById(R.id.tvRemark);
            recyclerView = view.findViewById(R.id.recyclerView);
            cardView = view.findViewById(R.id.cardView);
            ivEditPending = view.findViewById(R.id.ivEditPending);
            ivEditQG = view.findViewById(R.id.ivEditQG);
            ivEditPO = view.findViewById(R.id.ivEditPO);
            tvItems = view.findViewById(R.id.tvItems);
            llItems = view.findViewById(R.id.llItems);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_all_quotation_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //final QuotationHeaderDisp model




       final QuotationHeaderDisp model = quoteList.get(position);

        Log.e("ALL  ADAPTER LIST : ", " ------------------------------- " + model);
        Log.e("ALL  ADAPTER LIST1 : ", " ------------------------------- " + quoteList);
       // pdfModel=pdfList.get(position);

       // final DocumentHeader model1 = quoteTermList.get(position);

        holder.tvCustName.setText(model.getCustName());
        holder.tvQuoteNo.setText(model.getQuotNo());
        holder.tvDate.setText(model.getQuotDate());
      //  holder.tvRemark.setText(model.getOtherRemark1());
       // holder.tvRemark.setText(Integer.toString(model.getStatus()));
        if(model.getStatus()==0)
        {
            holder.tvRemark.setText("Pending");
        }else if(model.getStatus()==1)
        {
            holder.tvRemark.setText("Quotation Generated");
        }else if(model.getStatus()==2)
        {
            holder.tvRemark.setText("PO Generated");
        }

       if (model.getStatus()==0) {
            holder.ivEditPending.setVisibility(View.VISIBLE);
            holder.ivEditQG.setVisibility(View.GONE);
            holder.ivEditPO.setVisibility(View.GONE);
        } else if(model.getStatus()==1) {
            holder.ivEditQG.setVisibility(View.VISIBLE);
            holder.ivEditPending.setVisibility(View.GONE);
            holder.ivEditPO.setVisibility(View.GONE);
        }else if(model.getStatus()==2) {
            holder.ivEditPO.setVisibility(View.VISIBLE);
            holder.ivEditPending.setVisibility(View.GONE);
            holder.ivEditQG.setVisibility(View.GONE);
        }

//        if (model.getStatus() < 2) {
//            holder.ivEdit.setVisibility(View.VISIBLE);
//            holder.ivEdit1.setVisibility(View.GONE);
//        } else {
//            holder.ivEdit1.setVisibility(View.VISIBLE);
//            holder.ivEdit.setVisibility(View.GONE);
//        }

        if (model.getGetQuotDetailList() != null) {
            ArrayList<GetQuotDetail> detailList = new ArrayList<>();
            for (int i = 0; i < model.getGetQuotDetailList().size(); i++) {
                detailList.add(model.getGetQuotDetailList().get(i));
            }

            QuotationDetailListAdapter adapter = new QuotationDetailListAdapter(detailList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            holder.recyclerView.setLayoutManager(mLayoutManager);
            holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            holder.recyclerView.setAdapter(adapter);
        }


        if (model.getVisibleStatus() == 1) {
            holder.llItems.setVisibility(View.VISIBLE);
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
        } else {
            holder.llItems.setVisibility(View.GONE);
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
        }

        holder.tvItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getVisibleStatus() == 0) {
                    model.setVisibleStatus(1);
                    holder.llItems.setVisibility(View.VISIBLE);
                    holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
                } else if (model.getVisibleStatus() == 1) {
                    model.setVisibleStatus(0);
                    holder.llItems.setVisibility(View.GONE);
                    holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
                }
            }
        });

        holder.ivEditPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_po_quotation, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.action_pdf){
                            PdfData(model.getQuotHeadId());
                            //getQuotationTerm(model);
                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });
        holder.ivEditPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_quotation_pending, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {

                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            HomeActivity activity = (HomeActivity) context;

                            Fragment adf = new EditQuotationFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            args.putInt("type", model.getStatus());
                            args.putInt("allQuote", 1);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "AllQuotationListFragment").commit();

                        } else if (menuItem.getItemId() == R.id.action_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do you want to delete quotation?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteQuotation(model.getQuotHeadId());
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        holder.ivEditQG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_allquotation, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {

                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            HomeActivity activity = (HomeActivity) context;

                            Fragment adf = new EditQuotationFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            args.putInt("type", model.getStatus());
                            args.putInt("allQuote", 1);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "AllQuotationListFragment").commit();

                        } else if (menuItem.getItemId() == R.id.action_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do you want to delete quotation?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteQuotation(model.getQuotHeadId());
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else if(menuItem.getItemId() == R.id.action_pdf){

                            PdfData(model.getQuotHeadId());
                            //getQuotationTerm(model);

                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });




    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }

//int plantId=plant.getPlantId();
////    public void getQuotationTerm(final QuotationHeaderDisp quoteHeader,int plantId) {
////        if (Constants.isOnline(context)) {
////            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
////            commonDialog.show();
////
////            Call<ArrayList<DocumentHeader>> listCall = Constants.myInterface.getDocumentTerm(2,plantId);
////            listCall.enqueue(new Callback<ArrayList<DocumentHeader>>() {
////                @Override
////                public void onResponse(Call<ArrayList<DocumentHeader>> call, Response<ArrayList<DocumentHeader>> response) {
////                    try {
////                        if (response.body() != null) {
////
////                            Log.e("QUOTE TERM LIST : ", " - " + response.body());
////
////                            quoteTermList.clear();
////                            quoteTermNameList.clear();
////                            quoteTermDetailList.clear();
////
////                            quoteTermList = response.body();
////
////                           // createQuotationPDF(quoteHeader);
////
////                            commonDialog.dismiss();
////
////                        } else {
////                            commonDialog.dismiss();
////                            Log.e("Data Null : ", "-----------");
////                        }
////                    } catch (Exception e) {
////                        commonDialog.dismiss();
////                        Log.e("Exception : ", "-----------" + e.getMessage());
////                        e.printStackTrace();
////                    }
////                }
////
////                @Override
////                public void onFailure(Call<ArrayList<DocumentHeader>> call, Throwable t) {
////                    commonDialog.dismiss();
////                    Log.e("onFailure : ", "-----------" + t.getMessage());
////                    t.printStackTrace();
////                }
////            });
////        } else {
////            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
////        }
////    }

    private void PdfData(int quotHeadId) {

        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<PdfModel>> listCall = Constants.myInterface.getPdfData(quotHeadId);
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
                                Log.e("pdfModel","---------------pdfModel--------------------"+pdfModel);

//                                if (pdfModel.getQuotDetPrint() != null) {
//                                    ArrayList<QuotDetPrint> detailList = new ArrayList<>();
//                                    for (int i = 0; i < pdfModel.getQuotDetPrint().size(); i++) {
//                                        detailList.add(pdfModel.getQuotDetPrint().get(i));
//                                    }

//                                }


                                String plantStr = CustomSharedPreference.getString(context, CustomSharedPreference.KEY_PLANT);
                                Gson gsonPlant = new Gson();
                                plant = gsonPlant.fromJson(plantStr, Plant.class);


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
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    public void createQuotationPDF(PdfModel quoteHeader) {

        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
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
                float[] colWidth1 = new float[]{10,25, 30};
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
                cell.setHorizontalAlignment(3);
                pt.addCell(cell);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pdf_icon);
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

//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(1);
//                pdf.addCell(cell);

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

                cell = new PdfPCell(new Paragraph("" + quoteHeader.getCust().getCustAddress(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);


                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);


                //-------------------------------------------------------------------

                PdfPTable ptProject = new PdfPTable(2);
                ptProject.setWidthPercentage(100);

                if(!quoteHeader.getQuotDetPrint().get(0).getPlantName().equals("RMC")) {

                    float[] colProjWidth = new float[]{27, 50};
                    ptProject.setWidths(colProjWidth);
                    ptProject.setTotalWidth(colProjWidth);

                    cell = new PdfPCell(new Paragraph("Material Rate List With Carting Only For", textFontWithUnderline));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(0);
                    ptProject.addCell(cell);
                }else {

                    float[] colProjWidth = new float[]{25, 50};
                    ptProject.setWidths(colProjWidth);
                    ptProject.setTotalWidth(colProjWidth);

                    cell = new PdfPCell(new Paragraph("Quotation For Supply Of RMC At Your", textFontWithUnderline));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(0);
                    ptProject.addCell(cell);
                }

                cell = new PdfPCell(new Paragraph( quoteHeader.getProj().getProjName() + " site", boldFont1WithUnderline));
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


//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTo1.addCell(cell);

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

                cell = new PdfPCell(new Paragraph("Rate", textFont));
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

                cell = new PdfPCell(new Paragraph("3)Toll charges will be paid by you at an actual.\n", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("4)  OUR GST NO.27ADHFS8127N1ZE\n", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);


                cell = new PdfPCell(new Paragraph("5) Our Bank Details –"+quoteHeader.getComp().getCompName()+ "\n" +quoteHeader.getBank().getBankName()+ "\n" +"A/c. No." +quoteHeader.getBank().getAccNo(), boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

//                cell = new PdfPCell(new Paragraph("5) Pumping charges will be extra Rs.175/-\n", boldFont1));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                //cell.setColspan(1);
//                ptProject2.addCell(cell);
//
//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                ptProject2.addCell(cell);






                //--------------------------------------------------------------------------

                PdfPTable ptTerms = new PdfPTable(1);
                ptTerms.setWidthPercentage(100);

//                               cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTerms.addCell(cell);

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

//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBank.addCell(cell);
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

//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptThank.addCell(cell);

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

                cell = new PdfPCell(new Paragraph("For " +quoteHeader.getComp().getCompName(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProj.addCell(cell);


                cell = new PdfPCell(new Paragraph("Accepted By, ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
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

                //"For "+quoteHeader.getProj().getProjName()
                cell = new PdfPCell(new Paragraph("For "+quoteHeader.getProj().getProjName(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                //cell.setColspan(1);
                ptProj.addCell(cell);


//------------------------------------------------------------------------------

                PdfPTable ptBankDetail = new PdfPTable(1);
                ptBankDetail.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBankDetail.addCell(cell);

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

//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptBankDetail.addCell(cell);

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
                                Uri uri = FileProvider.getUriForFile(context, authorities, file1);
                                intent.setDataAndType(uri, "application/pdf");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.startActivity(intent);
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
            Toast.makeText(context, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteQuotation(int headerId) {
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.deleteQuotation(headerId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DELETE QUOTATION: ", " - " + response.body());

                            if (!response.body().isError()) {

                                HomeActivity activity = (HomeActivity) context;

                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new AllQuotationListFragment(), "HomeFragment");
                                ft.commit();


                            } else {
                                Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

}
