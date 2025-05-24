from django.urls import path
from frontend.views import advance_order_status, orders_list

urlpatterns = [
    path('orders/', orders_list, name='orders-list'),
    path('orders/<int:order_id>/next-status/', advance_order_status, name='advance-order-status')
]
