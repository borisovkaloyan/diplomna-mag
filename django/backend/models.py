from django.db import models
from django.contrib.auth.models import User


# Create your models here.

MENU_CATEGORIES = [
    ('Салата', 'Салата'),
    ('Супа', 'Супа'),
    ('Предястие', 'Прдеястие'),
    ('Основно', 'Основно'),
    ('Десерт', 'Десерт'),
    ('Напитка', 'Напитка')
]

class MenuItem(models.Model):
    name = models.CharField(max_length=100)
    description = models.TextField()
    price = models.DecimalField(max_digits = 5, decimal_places=2)
    image = models.ImageField(upload_to='menu_images/')
    category = models.CharField(
        max_length=50, 
        choices=MENU_CATEGORIES
    )
    is_vegetarian = models.BooleanField(default=False)

    def __str__(self):
        return self.name


class OrderItem(models.Model):
    order = models.ForeignKey('Order', on_delete=models.CASCADE, related_name='order_items')
    menu_item = models.ForeignKey('MenuItem', on_delete=models.CASCADE)
    quantity = models.PositiveIntegerField(default=1)

    def __str__(self):
        return f"{self.menu_item.name} x {self.quantity} (Order {self.order.id})"

class Order(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name='orders')
    order_date = models.DateTimeField(auto_now_add=True)
    total_amount = models.DecimalField(max_digits=7, decimal_places=2, default=0)
    items = models.ManyToManyField('MenuItem', through='OrderItem', related_name='orders')
    status = models.CharField(
        max_length=20,
        choices=[
            ('pending', 'Pending'),
            ('preparing', 'Preparing'),
            ('delivering', 'Delivering'),
            ('completed', 'Completed')
        ],
        default='pending'
    )
    delivery_address = models.CharField(max_length=255)

    def calculate_total(self):
        return sum(oi.menu_item.price * oi.quantity for oi in self.order_items.all())

    def __str__(self):
        return f"Order {self.id} by {self.user.username}"
