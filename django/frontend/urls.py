from django.urls import path
from frontend.views import CustomLoginView, CustomLogoutView, advance_order_status, orders_list

urlpatterns = [
    path('accounts/login/', CustomLoginView.as_view(), name='login'),
    path('accounts/logout/', CustomLogoutView.as_view(), name='logout'),
    path('orders/', orders_list, name='orders-list'),
    path('orders/<int:order_id>/next-status/', advance_order_status, name='advance-order-status')
]
