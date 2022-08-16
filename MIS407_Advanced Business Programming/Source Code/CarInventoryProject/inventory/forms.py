# ----------------------------------------------------------------------------
# Subject: MIS407
# Group  : 3
# Author : Yen Vo
# Date   : 04/25/2022
# version: 1.0
# ---------------------------------------------------------------------------
""" Define all forms used by the Inventory system"""
# ---------------------------------------------------------------------------
# Changes
# 04/25/2022  Yen Vo             - Created VehicleForm and BrandForm
#
# ---------------------------------------------------------------------------

from django import forms
from .models import Brand, Vehicle


class VehicleForm(forms.ModelForm):
    class Meta:
        model = Vehicle
        fields = ['brand', 'model_name', 'model_year', 'title_status', 'vin_number', 'price', 'mileage',
                  'engine', 'color', 'status', 'id']

    STATUS_CHOICES = (
        ('Available', 'Available'),
        ('Pending', 'Pending'),
        ('Sold', 'Sold')
    )

    TITLE_STATUS_CHOICES = (
        ('Clear', 'Clear'),
        ('Salvage', 'Salvage'),
        ('Junk', 'Junk'),
        ('Bonded', 'Bonded'),
        ('Reconstructed', 'Reconstructed')
    )

    ENGINE_CHOICES = (
        ('2 Cyl', '2 Cyl'),
        ('4 Cyl', '4 Cyl'),
        ('6 Cyl', '6 Cyl'),
        ('8 Cyl', '8 Cyl')
    )

    model_name = forms.CharField(widget=forms.TextInput)
    model_year = forms.CharField(widget=forms.DateInput)
    title_status = forms.ChoiceField(choices=TITLE_STATUS_CHOICES)
    vin_number = forms.CharField(widget=forms.TextInput)
    price = forms.FloatField(widget=forms.TextInput)
    mileage = forms.IntegerField(widget=forms.TextInput)
    engine = forms.ChoiceField(choices=ENGINE_CHOICES)
    color = forms.CharField(widget=forms.TextInput)
    status = forms.ChoiceField(choices=STATUS_CHOICES)


class BrandForm(forms.ModelForm):
    class Meta:
        model = Brand
        fields = ['brand_name', 'country']

    brand_name = forms.CharField(widget=forms.TextInput)
    country = forms.CharField(max_length=100)
