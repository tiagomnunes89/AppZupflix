package br.com.zupfilms.ui.home.homeActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import br.com.zupfilms.R;

class HomeActivityViewHolder {

    final BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    private final DrawerLayout drawerLayout;
    final TextView titleToolBar;

    HomeActivityViewHolder(View view) {
        drawerLayout = view.findViewById(R.id.drawer_layout);
        CoordinatorLayout homeDrawer = view.findViewById(R.id.home_drawer);
        bottomNavigationView = homeDrawer.findViewById(R.id.botton_nav_view);
        titleToolBar = homeDrawer.findViewById(R.id.textHomeTitle);
    }
}
