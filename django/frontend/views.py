from django.shortcuts import render
from backend.models import Order

def orders_list(request):
    orders = Order.objects.select_related('user').prefetch_related('items').all()
    return render(request, 'frontend/orders_list.html', {'orders': orders})
