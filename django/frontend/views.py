from django.shortcuts import redirect, render, get_object_or_404
from django.urls import reverse_lazy
from django.utils.dateparse import parse_date
from backend.models import Order
from datetime import date
from django.db.models.functions import TruncDate
from django.contrib.auth.decorators import login_required
from django.contrib.auth.views import LoginView, LogoutView 

class CustomLoginView(LoginView):
    template_name = 'registration/login.html'

class CustomLogoutView(LogoutView):
    next_page = reverse_lazy('login')  # or wherever you want to redirect after logout


# Status transition logic
STATUS_FLOW = {
    'Изчакване': 'Приготвяне',
    'Приготвяне': 'Доставяне',
    'Доставяне': 'Завършена'
}

@login_required
def orders_list(request):
    # Get distinct dates with orders
    available_dates = list(
        Order.objects.annotate(order_day=TruncDate('order_date'))
        .values_list('order_day', flat=True)
        .distinct()
    )

    selected_date = request.GET.get('date')
    filter_date = parse_date(selected_date) if selected_date else date.today()

    orders = Order.objects.select_related('user').prefetch_related('items').filter(order_date__date=filter_date)

    grouped_orders = {
        'Изчакване': orders.filter(status='Изчакване'),
        'Приготвяне': orders.filter(status='Приготвяне'),
        'Доставяне': orders.filter(status='Доставяне'),
        'Завършена': orders.filter(status='Завършена'),
    }

    total_money = sum(order.total_amount for order in orders)

    return render(
        request,
        'frontend/orders_list.html',
        {
            'grouped_orders': grouped_orders,
            'STATUS_FLOW': STATUS_FLOW,
            'selected_date': filter_date,
            'today': date.today(),
            'available_dates': [d.isoformat() for d in available_dates],  # Send to JS
            'total_amount_for_day': total_money
        }
    )

@login_required
def advance_order_status(request, order_id):
    order = get_object_or_404(Order, id=order_id)
    next_status = STATUS_FLOW.get(order.status)
    if next_status:
        order.status = next_status
        order.save()
    return redirect('orders-list')

