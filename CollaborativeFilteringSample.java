import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CollaborativeFilteringSample {
  public static void main(String[] args) {
    try{
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("data/rating.csv")));
      String str;
      List<String> ranking = new ArrayList<String>();
      while( (str = reader.readLine() ) != null){
        String[] subs = str.split(",",3);
        if(subs[2] != "-1"){
          ranking.add(str);
        }
      }

      BufferedWriter writer = new BufferedWriter(new FileWriter("data/data.csv"));
      for (String rank : ranking) {
        writer.write(rank);
      }

      DataModel model = new FileDataModel(new File("data/data.csv"));

      //相関性の評価基準の設定
      UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

      //評価の近い人を探すロジック
      UserNeighborhood neighborhood = new NearestNUserNeighborhood(3,similarity,model);

      Recommender recommender = new GenericUserBasedRecommender(model,neighborhood,similarity);

      List<RecommendedItem> recommendations = recommender.recommend(1,1);
      for (RecommendedItem recommendation : recommendations){
        System.out.println(recommendation);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TasteException e) {
      //line 15
      e.printStackTrace();
    }
  }
}
