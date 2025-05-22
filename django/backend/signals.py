from django.db.models.signals import m2m_changed
from django.dispatch import receiver
from .models import Order

@receiver(m2m_changed, sender=Order.items.through)
def update_total_amount(sender, instance, action, **kwargs):
    if action in ['post_add', 'post_remove', 'post_clear']:
        total = instance.calculate_total()
        Order.objects.filter(id=instance.id).update(total_amount=total)
