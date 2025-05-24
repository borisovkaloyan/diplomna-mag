from django.shortcuts import redirect, render, get_object_or_404
from django.utils.dateparse import parse_date
from backend.models import Order
from datetime import date
from django.db.models.functions import TruncDate

# Status transition logic
STATUS_FLOW = {
    'pending': 'preparing',
    'preparing': 'delivering',
    'delivering': 'completed'
}


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
        'Pending': orders.filter(status='pending'),
        'Preparing': orders.filter(status='preparing'),
        'Delivering': orders.filter(status='delivering'),
        'Completed': orders.filter(status='completed'),
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


def advance_order_status(request, order_id):
    order = get_object_or_404(Order, id=order_id)
    next_status = STATUS_FLOW.get(order.status)
    if next_status:
        order.status = next_status
        order.save()
    return redirect('orders-list')

