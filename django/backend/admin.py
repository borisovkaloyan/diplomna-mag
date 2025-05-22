from django.contrib import admin
from django.utils.html import format_html

from backend.models import MenuItem, Order

# Register your models here.

class MenuItemAdmin(admin.ModelAdmin):
    def image_tag(self, obj):
        return format_html(f'<img src="{obj.image.url}" height=64 alt={obj.name} />')

    image_tag.short_description = 'Image'

    list_display = ('name', 'description', 'price', 'category', 'is_vegetarian', 'image_tag')
    search_fields = ('name', 'description')
    list_filter = ('category', 'is_vegetarian')
    ordering = ('name',)  

class OrderAdmin(admin.ModelAdmin):
    def menu_items_list(self, obj):
        return ", ".join([item.name for item in obj.items.all()])

    def customer_name(self, obj):
        return obj.user.get_full_name() or obj.user.username

    def customer_email(self, obj):
        return obj.user.email

    menu_items_list.short_description = 'Menu Items'
    customer_name.short_description = 'Customer Name'
    customer_email.short_description = 'Customer Email'

    list_display = ('id', 'customer_name', 'customer_email', 'order_date', 'total_amount', 'menu_items_list', 'status', 'delivery_address')
    search_fields = ('user__username', 'user__first_name', 'user__last_name', 'user__email', 'status')
    list_filter = ('order_date',)
    ordering = ('-order_date',)

admin.site.register(MenuItem, MenuItemAdmin)
admin.site.register(Order, OrderAdmin)
