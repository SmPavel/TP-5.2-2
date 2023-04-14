from django.db import models


class Group(models.Model):
    """ Модель группы пользователей приложения """
    gr_name = models.CharField(max_length=100)

    class Meta:
        db_table = 'groups'


