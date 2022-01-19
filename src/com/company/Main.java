package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class Employee{
    public Integer emp_id;
    public String surname;
    public String name;
    public String date_strt;
    public String date_end;
    public Integer otpusk;
    public Integer otguli;
    public Date strt_date;
    public Date end_date;
    public Date date_now;
    public Integer days_delta;
    public Integer bns;

    public Employee(Scanner sc) throws ParseException {
        this.emp_id = Integer.valueOf(sc.next());
        this.surname = sc.next();
        this.name = sc.next();
        this.date_strt = sc.next();
        this.date_end = sc.next();
        this.otpusk = Integer.valueOf(sc.next());
        this.otguli = Integer.valueOf(sc.next().split("$")[0]);
        String strt = this.date_strt;
        String end = this.date_end;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        this.strt_date = formatter.parse(strt);
        this.end_date = formatter.parse(end);
        long milliseconds = end_date.getTime() - strt_date.getTime();
        this.days_delta = (int) (milliseconds / (24 * 60 * 60 * 1000));

        String dt_now = LocalDate.now().toString();
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        this.date_now = formatter2.parse(dt_now);

        Integer mounthes = (int) ((end_date.getTime() - date_now.getTime()) / (24 * 60 * 60 * 1000) / (30));
        this.bns = otpusk - (mounthes * 2);
    }
    @Override
    public String toString(){
        return emp_id + ";" + surname + ";" + name + ";" + date_strt + ";" + date_end + ";" + otpusk + ";" + otguli;
    }
}


class Request{
    public Integer request_id;
    public Integer emp_id;
    public Integer daysoff_id;
    public Integer days;

    public Request(Scanner sc){
        this.request_id = Integer.valueOf(sc.next());
        this.emp_id = Integer.valueOf(sc.next());
        this.daysoff_id = Integer.valueOf(sc.next());
        this.days = Integer.valueOf(sc.next().split("$")[0]);
    }

    @Override
    public String toString(){
        return request_id + ";" + emp_id + ";" + daysoff_id + ";" + days;
    }
}

public class Main {

    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException {
        Scanner sc1 = new Scanner(new File("input1.txt"));
        sc1.useDelimiter("[;\\n]");
        ArrayList<Employee> emp = new ArrayList<>();
        while(sc1.hasNext()){
            emp.add(new Employee(sc1));
        }
        Scanner sc2 = new Scanner(new File("input2.txt"));
        sc2.useDelimiter("[;\\n]");
        ArrayList<Request> reqs = new ArrayList<>();
        while (sc2.hasNext()){
            reqs.add(new Request(sc2));
        }
        ArrayList<Employee> out1 = new ArrayList<>();
        ArrayList<Integer> out1_i = new ArrayList<>();
        PrintWriter pw1 = new PrintWriter(new File("output1.txt"));
        for(int i = 0; i < reqs.size(); i++){
            Integer emp_id = reqs.get(i).emp_id;
            Integer days = reqs.get(i).days;
            if(reqs.get(i).daysoff_id == 1){
                if(emp.stream().filter(x-> x.emp_id.equals(emp_id) & x.otpusk < days).collect(Collectors.toList()).size() != 0){
                    out1.add(emp.stream().filter(x-> x.emp_id.equals(emp_id) & x.otpusk < days).collect(Collectors.toList()).get(0));
                    out1_i.add(i);
                }
            }
            else{
                if(emp.stream().filter(x-> x.emp_id.equals(emp_id) & x.otguli < days).collect(Collectors.toList()).size() != 0){
                    out1.add(emp.stream().filter(x-> x.emp_id.equals(emp_id) & x.otguli < days).collect(Collectors.toList()).get(0));
                    out1_i.add(i);
                }
            }
        }
        for(int i = 0; i < reqs.size(); i++){
            if(i == out1_i.size() - 1){
                if(out1_i.contains(i)){
                    pw1.print(reqs.get(i));
                    break;
                }
            }
            if(out1_i.contains(i)){
                pw1.println(reqs.get(i));
            }
        }
        pw1.flush();

        Scanner sc3 = new Scanner(new File("input3.txt"));
        Integer days_in = Integer.valueOf(sc3.next());
        ArrayList<Employee> out2 = new ArrayList<>();
        out2 = (ArrayList<Employee>) emp.stream().filter(x-> x.otpusk >= days_in).collect(Collectors.toList());
        Comparator<Employee> cmp2 = new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.surname.compareTo(o2.surname);
            }
        };
        Collections.sort(out2, cmp2);
        PrintWriter pw2 = new PrintWriter(new File("output2.txt"));
        for(int i = 0; i < out2.size(); i++){
            if(i == out2.size() - 1){
                pw2.print(out2.get(i).surname + ";" + out2.get(i).name + ";" + out2.get(i).otpusk);
                break;
            }
            pw2.println(out2.get(i).surname + ";" + out2.get(i).name + ";" + out2.get(i).otpusk);
        }
        pw2.flush();

        Comparator<Employee> cmp3 = new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                if(o1.days_delta.compareTo(o2.days_delta) == 0){
                    return o1.surname.compareTo(o2.surname);
                }
                return -o1.days_delta.compareTo(o2.days_delta);
            }
        };
        Collections.sort(emp, cmp3);
        PrintWriter pw3 = new PrintWriter(new File("output3.txt"));
        for(int i = 0; i < emp.size(); i++){
            if(i == emp.size() - 1){
                pw3.print(emp.get(i).surname + ";" + emp.get(i).name + ";" + emp.get(i).days_delta);
                break;
            }
            pw3.println(emp.get(i).surname + ";" + emp.get(i).name + ";" + emp.get(i).days_delta);
        }
        pw3.flush();

        Scanner sc4 = new Scanner(new File("input4.txt"));
        Integer empId_in = Integer.valueOf(sc4.next());

        PrintWriter pw4 = new PrintWriter(new File("output4.txt"));
        List<Employee> out4;
        out4 = emp.stream().filter(x-> x.emp_id.equals(empId_in)).collect(Collectors.toList());
        if(out4.size() != 0){
            if(out4.get(0).bns >= 0){
                pw4.print(out4.get(0).bns);
            }
        }
        else{
            pw4.print(-1);
        }
        pw4.flush();
    }
}
