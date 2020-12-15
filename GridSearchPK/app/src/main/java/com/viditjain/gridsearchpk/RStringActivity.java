package com.viditjain.gridsearchpk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RStringActivity extends AppCompatActivity
{
    GridAdapter gridAdapter;
    ArrayList<String> lettersArrayList;
    GridView buttonsGrid;
    ImageButton backspaceButton;
    Button loginButton;
    static TextView rStringTextView;
    static char[][] grid;
    static int gridSize=5;
    static String registeredPswd;
    static int horizontal;
    static int vertical;
    static int horSteps;
    static int verSteps;
    String user_name;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rstring);

        intent=getIntent();
        registeredPswd=intent.getStringExtra("r_string");
        user_name=intent.getStringExtra("user_name");
        if(intent.getStringExtra("dir1").equals("Up"))
        {
            vertical=-1;
            verSteps=Integer.parseInt(intent.getStringExtra("dir1_steps"));
        }
        else if(intent.getStringExtra("dir1").equals("Down"))
        {
            vertical=1;
            verSteps=Integer.parseInt(intent.getStringExtra("dir1_steps"));
        }

        if(intent.getStringExtra("dir2").equals("Left"))
        {
            horizontal=-1;
            horSteps=Integer.parseInt(intent.getStringExtra("dir2_steps"));
        }
        else if(intent.getStringExtra("dir2").equals("Right"))
        {
            horizontal=1;
            horSteps=Integer.parseInt(intent.getStringExtra("dir2_steps"));
        }

        buttonsGrid=findViewById(R.id.buttons_grid);
        backspaceButton=findViewById(R.id.backspace_button);
        rStringTextView=findViewById(R.id.r_string_textview);
        loginButton=findViewById(R.id.submit_button);

        buttonsGrid.setNumColumns(gridSize);
        char[][] arr=create_random_matrix(gridSize);
        grid=arr;

        lettersArrayList=flattenGrid(grid);
        gridAdapter=new GridAdapter(this,lettersArrayList);
        buttonsGrid.setAdapter(gridAdapter);
        backspaceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(rStringTextView.getText().toString().length()>0)
                {
                    rStringTextView.setText(rStringTextView.getText().subSequence(0,rStringTextView.getText().length()-1));
                }
            }
        });

        backspaceButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if(rStringTextView.getText().toString().length()>0)
                {
                    rStringTextView.setText("");
                }
                return true;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(check(rStringTextView.getText().toString()))
                {
                    Intent intent=new Intent(RStringActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("user_name",user_name);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(RStringActivity.this,"Incorrect R-String",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static char findCharAtPos(int x,int y)
    {

        int upd_x=(x+vertical*verSteps+gridSize)%gridSize;
        int upd_y=(y+horizontal*horSteps+gridSize)%gridSize;
        return grid[upd_x][upd_y];
    }

    public static ArrayList<Character> uniqueCharacters(String test)
    {
        ArrayList<Character> vowels = new ArrayList<>();
        String temp="";
        for(int i=0; i<test.length();i++)
        {
            char current=test.charAt(i);
            if(temp.indexOf(current)<0)
            {
                temp=temp+current;
                vowels.add(current);
            }
        }

        System.out.println(temp+" ");
        return vowels;
    }

    public static char[][] create_random_matrix(int gridSize)
    {
        char[][] arr=new char[gridSize][gridSize];

        // Get all unique characters from the registered password
        ArrayList<Character> uniq_char=uniqueCharacters(registeredPswd);

        // Get all the rest of the unique characters in the alphabet
        ArrayList<Character> restOfUnique=new ArrayList<>();
        for(char c = 'A'; c <= 'Z'; ++c)
        {
            int i;
            boolean check = true;
            for (i=0;i<uniq_char.size();i++)
            {
                if(c==uniq_char.get(i))
                {
                    check=false;
                    break;
                }
            }
            if(check)
            {
                restOfUnique.add(c);
            }
        }
        // Shuffle the rest of the unique characters
        Collections.shuffle(restOfUnique);

        int empty_slots_left = gridSize * gridSize - uniq_char.size();

        for(int j=0;j<empty_slots_left;j++)
        {
            uniq_char.add(restOfUnique.get(j));
        }
        Collections.shuffle(uniq_char);

        for(int i=0; i < gridSize;i++) {
            for (int j = 0; j < gridSize; j++) {
                arr[i][j] = uniq_char.get(j * gridSize + i);
            }
        }
        Log.d("ghghgh",Arrays.deepToString(arr));
        return arr;
    }

    public static boolean check(String enteredPswd){

        /**
         * @param enteredPswd		Password entered by the user
         * @param registeredPswd	Registered password of the user
         * @param grid				Grid generated by the system
         * @param horizontal		Registered Horizontal direction; -1 indicates left; 1 indicates right
         * @param vertical			Registered Vertical direction; -1 indicates up; 1 indicates down
         * @param horSteps			Number of registered steps in horizontal direction
         * @param verSteps			Number of registered steps in vertical direction
         * @return 					The entered password is correct or not
         */

        // ASSUMPTION: All grid characters are distinct

        if(enteredPswd.length()!=registeredPswd.length()) {
            return false;
        }

        gridSize = grid.length;

        for(int p=0;p<registeredPswd.length();p++) {

            char chReg = registeredPswd.charAt(p);

            for(int i=0;i<gridSize;i++) {

                boolean found = false;

                for(int j=0;j<gridSize;j++) {

                    if(grid[i][j]==chReg) {

                        char chAct = findCharAtPos(i,j);

                        if(chAct!=enteredPswd.charAt(p)) {
                            return false;
                        }

                        found = true;
                        break;
                    }

                }

                if(found) {
                    break;
                }
            }

        }

        return true;
    }

    public ArrayList<String> flattenGrid(char[][] grid)
    {
        ArrayList<String> flatGrid=new ArrayList<>();
        for(int i=0;i<grid.length;i++)
        {
            for(int j=0;j<grid[0].length;j++)
            {
                flatGrid.add(grid[i][j]+"");
            }
        }
        return flatGrid;
    }
}
