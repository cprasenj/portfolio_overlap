package org.portfolio_overlap;

import org.portfolio_overlap.application.FundOverlap;
import org.portfolio_overlap.dao.FileSystemFundsDao;
import org.portfolio_overlap.service.PortfolioOverlapService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Geektrust {
    public static void main(String[] args) throws IOException {
        FundOverlap fundOverlap = new FundOverlap(new ArrayList<>(), new PortfolioOverlapService(new FileSystemFundsDao(null)));
        File file = new File(args[0]);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            List<String> results = fundOverlap.execute(st);
            for (String result: results) {
                System.out.println(result);
            }
        }
    }
}