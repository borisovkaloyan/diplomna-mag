from django.contrib.auth.models import User
from rest_framework import serializers
from django.contrib.auth import authenticate

from backend.models import MenuItem, Order, OrderItem, MENU_CATEGORIES


class MenuItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = MenuItem
        fields = '__all__'

class OrderItemSerializer(serializers.ModelSerializer):
    menu_item = MenuItemSerializer(read_only=True)
    # menu_item_id = serializers.PrimaryKeyRelatedField(queryset=MenuItem.objects.all(), source='menu_item', write_only=True)

    class Meta:
        model = OrderItem
        fields = ['menu_item', 'quantity']

class OrderRequestSerializer(serializers.Serializer):
    user = serializers.IntegerField(required=True)
    delivery_address = serializers.CharField(max_length=255, required=True)
    items = serializers.ListField(
        child=serializers.IntegerField(), write_only=True, required=True
    )

    def validate_user_id(self, value):
        if not User.objects.filter(id=value).exists():
            raise serializers.ValidationError("User does not exist.")
        return value

class OrderSerializer(serializers.ModelSerializer):
    order_items = OrderItemSerializer(many=True, read_only=True)
    items = serializers.ListField(
        child=serializers.IntegerField(), write_only=True, required=False
    )

    class Meta:
        model = Order
        fields = ['id', 'user', 'order_date', 'total_amount', 'status', 'delivery_address', 'order_items', 'items']

    def create(self, validated_data):
        items = validated_data.pop('items', [])
        order = Order.objects.create(**validated_data)
        # Count quantities
        from collections import Counter
        item_counts = Counter(items)
        for item_id, quantity in item_counts.items():
            OrderItem.objects.create(order=order, menu_item_id=item_id, quantity=quantity)
        order.total_amount = order.calculate_total()
        order.save()
        return order

class OrderCategorySerializer(serializers.Serializer):
    category = serializers.CharField(required=True)


class UserRegistrationSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True)

    class Meta:
        model = User
        fields = ['username', 'password', 'first_name', 'last_name', 'email']

    def create(self, validated_data):
        user = User.objects.create_user(**validated_data)
        return user
    
    def validate_email(self, value):
        if User.objects.filter(email=value).exists():
            raise serializers.ValidationError("Email already in use.")
        return value
    
class UserRegistrationResponseSerializer(serializers.Serializer):
    message = serializers.CharField(default="User registered successfully")
    username = serializers.CharField(source='user.username')
    first_name = serializers.CharField(source='user.first_name')
    last_name = serializers.CharField(source='user.last_name')

    class Meta:
        fields = ['message', 'username', 'first_name', 'last_name']

class UserLoginSerializer(serializers.Serializer):
    username = serializers.CharField()
    password = serializers.CharField(write_only=True)

    class Meta:
        fields = ['username', 'password']

    def validate(self, data):
        user = authenticate(**data)
        if user and user.is_active:
            return user
        raise serializers.ValidationError("Invalid username or password")
    
class UserLoginResponseSerializer(serializers.Serializer):
    user_id = serializers.IntegerField(source='id')
    username = serializers.CharField()
    first_name = serializers.CharField()
    last_name = serializers.CharField()

    class Meta:
        fields = ['user_id', 'username', 'first_name', 'last_name']

class OrdersByUserSerializer(serializers.Serializer):
    user_id = serializers.IntegerField(required=True)

    class Meta:
        fields = ['user_id']

    def validate_user_id(self, value):
        if not User.objects.filter(id=value).exists():
            raise serializers.ValidationError("User does not exist.")
        return value
    
class MenuItemByIdSerializer(serializers.Serializer):
    id = serializers.IntegerField(required=True)

    class Meta:
        fields = ['id']

    def validate_id(self, value):
        if not MenuItem.objects.filter(id=value).exists():
            raise serializers.ValidationError("Menu item does not exist.")
        return value