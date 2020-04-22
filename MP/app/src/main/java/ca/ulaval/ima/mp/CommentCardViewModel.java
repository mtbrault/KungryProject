package ca.ulaval.ima.mp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModel;

public class CommentCardViewModel extends CardView {
    CommentCardViewModel(Context context, Restaurant.Review review) throws ParseException {
        super(context);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.comment_card_view, this, false);
        TextView dateTextView = view.findViewById(R.id.date);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextView creator = view.findViewById(R.id.creator);
        TextView comment = view.findViewById(R.id.comment);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE);
        Date date = formatter.parse(review.date);
        dateTextView.setText(formatter2.format(date));
        ratingBar.setRating(Integer.parseInt(review.stars));
        creator.setText(review.creator.firstName + " " + review.creator.lastName);
        comment.setText(review.comment == "null" ? "aucun commentaire" : review.comment);
        addView(view);
    }

}
