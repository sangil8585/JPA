package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

//        이런 식으로 값을 다체우는 로직을 개발 할 수 있지만 문제는 여기 로직에서 이렇게하고
//        저기 로직에서 이렇게 해버리니까 퍼져서 유지보수가 어려워짐. 로직이 분산됨. 따라서
//        OrderItem에서 protected로 생성자를 만들어서 여기서 사용하지 못하게 만듬
//        OrderItem orderItem1 = new OrderItem();
//        orderItem1.setCount();

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
        // 엔티티에 핵심비즈니스 로직을 가지고 객체지향의 특성을 적극 활용한 것을 '도메인 모델 패턴'이라함

        // 데이터베이스를 직접적으로 다루는 라이브러리 mybatis나
        // jdbc템플릿 아니면 내가 직접 SQL을 변경하면
        // 쿼리를 직접 짜서 날려야됨.
        // 아이템 제고를 다시 올리는 로직을 짜서 올려야 됨.
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
