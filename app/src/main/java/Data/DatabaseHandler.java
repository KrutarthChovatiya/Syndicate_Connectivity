package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Modal.Infromation;
import Utils.Util;

;

public class DatabaseHandler extends SQLiteOpenHelper
{
    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME,null,Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String CREATE_Informaton_table="CREATE TABLE "+Util.TABLE_NAME+"("+Util.Key_Id+" INTEGER PRIMARY KEY, "+Util.Operator_Name+" TEXT,"+Util.Latitude+" TEXT,"+Util.Longitude+" TEXT"+")";

        sqLiteDatabase.execSQL(CREATE_Informaton_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Util.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

    public void addInformation(Infromation infromation)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues value =new ContentValues();
        value.put(Util.Operator_Name, Infromation.getOperatorName());
        value.put(Util.Latitude,Infromation.getLatitude());
        value.put(Util.Longitude,Infromation.getLongitude());

        db.insert(Util.TABLE_NAME,null,value);
        db.close();
    }

    public Infromation getInformation(int id)
    {
        SQLiteDatabase db  =this.getReadableDatabase();
        Cursor cursers=db.query(Util.TABLE_NAME,new String[]{Util.Key_Id,Util.Operator_Name,Util.Latitude,Util.Longitude},Util.Key_Id+"=?",new String[]{String.valueOf(id)},null,null,null,null);

        if(cursers!=null)
            cursers.moveToFirst();

        Infromation infromation=new Infromation(Integer.parseInt(cursers.getString(0)),cursers.getString(1),cursers.getString(2),cursers.getString(3));

        return infromation;
    }

    public List<Infromation> getAllInformation()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        List<Infromation> infolist=new ArrayList<>();

        String selectAll="SELECT * FROM "+Util.TABLE_NAME;
        Cursor cursor=db.rawQuery(selectAll,null);

        if(cursor.moveToNext())
        {
            do{
                Infromation information=new Infromation();
                information.setId(Integer.parseInt(cursor.getString(0)));
                information.setOperatorName(cursor.getString(1));
                information.setLatitude(cursor.getString(2));
                information.setLongitude(cursor.getString(3));

                infolist.add(information);
            }while (cursor.moveToNext());
        }
        return infolist;
    }
}
