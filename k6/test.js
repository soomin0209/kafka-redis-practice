import http from 'k6/http';

export const options = {
    vus: 5,
    duration: '20s',
};

export default function () {
    const orderId = Math.floor(Math.random() * 1000000);
    const paymentId = Math.floor(Math.random() * 1000000);

    const payload = JSON.stringify({
        orderId: orderId,
        paymentId: paymentId,
        productId: 999,
        userId: 1,
        category: "FOOD",
        quantity: 1
    });

    const params = {
        headers: { 'Content-Type': 'application/json' }
    };

    http.post(
        "http://host.docker.internal:8080/api/payment/completion",
        payload,
        params
    );
}

// delivery-group → DB 적재
// payment-history-group → DB 적재
// product-ranking-group → Redis 적재 !!

// Redis가 DB보다 압도적으로 빨리 값을 읽고 쓰는 캐시 DB이기 때문에
// product-ranking-group이 빨리 처리가 끝남