package ru.aglophotis.mirea.microservice.cart.dao;

import org.hibernate.Session;
import ru.aglophotis.mirea.microservice.cart.entities.CartItem;
import ru.aglophotis.mirea.microservice.cart.utils.HibernateSessionFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class CartDao {

    public List<CartItem> getAll(int authorId) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CartItem where authorId = :authorId")
                    .setParameter("authorId", authorId)
                    .list();
        }
    }

    public int deleteById(int cartItemId, int authorId) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            int id = session.createQuery("delete from CartItem where authorId = :authorId and id = :cartItemId")
                    .setParameter("authorId", authorId)
                    .setParameter("cartItemId", cartItemId)
                    .executeUpdate();
            session.getTransaction().commit();
            return id;
        }
    }

    public List<CartItem> getAllDistinct(int authorId) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            List<CartItem> list = session.createQuery("from CartItem where authorId = :id")
                    .setParameter("id", authorId)
                    .list();
            ArrayList<CartItem> resultList = new ArrayList<>();
            for (CartItem item : list) {
                boolean isContains = false;
                for (CartItem resultItem : resultList) {
                    if (item.getItemId() == resultItem.getItemId()) {
                        isContains = true;
                        break;
                    }
                }
                if (!isContains) {
                    resultList.add(item);
                }
            }
            return resultList;
        }
    }

    public int getCountItems(int itemId, int authorId) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return ((Long) session
                    .createQuery("select count(*) from CartItem where authorId = :authorId and itemId = :itemId")
                    .setParameter("authorId", authorId)
                    .setParameter("itemId", itemId)
                    .uniqueResult()).intValue();
        }
    }

    public int insert(CartItem item) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            int id = (int) session.save(item);
            session.getTransaction().commit();
            return id;
        }
    }

    public void deleteAll(int authorId) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.createQuery("DELETE FROM CartItem where authorId = :authorId")
                    .setParameter("authorId", authorId)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    public void delete(CartItem item) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.delete(item);
            session.getTransaction().commit();
        }
    }

    public void update(CartItem item) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.update(item);
            session.getTransaction().commit();
        }
    }
}
