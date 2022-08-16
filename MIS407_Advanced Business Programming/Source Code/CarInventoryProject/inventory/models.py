# ----------------------------------------------------------------------------
# Subject: MIS407
# Group  : 3
# Author : Yen Vo
# Date   : 04/25/2022
# version: 1.0
# ---------------------------------------------------------------------------
""" Define all Models used by the Inventory system"""
# ---------------------------------------------------------------------------
# Changes
# 04/25/2022  Yen Vo - Created Brand and Vehicle models
#
# ---------------------------------------------------------------------------

from django.db import models

# Create your models here.
from django.urls import reverse


# Create model Brand
class Brand(models.Model):
    brand_name = models.CharField(max_length=100, unique=True)
    country = models.CharField(max_length=100)

    class Meta:
        ordering = ('brand_name',)

    def get_name(self):
        return self.brand_name

    def __str__(self):
        return self.brand_name


# Create model for Vehicle

class Vehicle(models.Model):
    brand = models.ForeignKey(Brand, on_delete=models.CASCADE)
    model_name = models.CharField(max_length=150)
    model_year = models.IntegerField()
    title_status = models.CharField(max_length=150)
    vin_number = models.CharField(max_length=150)
    price = models.FloatField()
    mileage = models.IntegerField()
    color = models.CharField(max_length=50)
    engine = models.CharField(max_length=100)
    status = models.CharField(max_length=100)

    class Meta:
        ordering = ('model_name',)

    def __str__(self):
        return self.model_name + ' - ' + str(self.model_year)

    def get_url(self):
        return reverse('inventory_detail', args=[self.id])
