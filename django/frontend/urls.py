from django.urls import path
from frontend.views import orders_list

urlpatterns = [
    path('orders/', orders_list, name='orders-list'),
]
