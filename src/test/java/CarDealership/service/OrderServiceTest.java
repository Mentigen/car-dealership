package CarDealership.service;

import CarDealership.domain.car.*;
import CarDealership.infrastructure.repository.InMemoryCarModelRepository;
import CarDealership.infrastructure.repository.InMemoryOrderRepository;
import CarDealership.infrastructure.repository.InMemoryPartRepository;
import CarDealership.infrastructure.repository.InMemoryUserRepository;
import org.example.domain.car.*;
import CarDealership.domain.order.CustomOrder;
import CarDealership.domain.order.StockOrder;
import CarDealership.domain.user.Role;
import CarDealership.domain.user.User;
import org.example.infrastructure.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {
  private OrderService orderService;
  private InMemoryOrderRepository orderRepository;
  private InMemoryUserRepository userRepository;
  private InMemoryCarModelRepository carModelRepository;
  private InMemoryPartRepository partRepository;

  private User client;
  private User manager;

  private CarModel model;
  private Part wheelPart;
  private Part transmissionPart;
  private Part steeringWheelPart;
  private Part interiorPart;
  private Part colorPart;

  @BeforeEach
  public void setUp() {
    orderRepository = new InMemoryOrderRepository();
    userRepository = new InMemoryUserRepository();
    orderService = new OrderService(orderRepository, userRepository);
    partRepository = new InMemoryPartRepository();
    carModelRepository = new InMemoryCarModelRepository();

    client =
        new User(
            UUID.randomUUID(),
            "Ivan",
            "Ivanov",
            Role.CUSTOMER,
            "ivan@ya.ru",
            "88005553535",
            "ivan228");

    manager =
        new User(
            UUID.randomUUID(), "Petr", "Petrov", Role.MANAGER, "petr@gmail.com", "001", "petr007");

    userRepository.save(client);
    userRepository.save(manager);

    setupCarData();
  }

  public void setupCarData() {
    model = new CarModel();
    model.setId(UUID.randomUUID());
    model.setBrand("BMW");
    model.setModelName("320i");
    model.setEnginePower(new EnginePower(184));
    model.setEngineVolume(new EngineVolume(2.0));
    model.setFuel(FuelType.PETROL);
    model.setBody(BodyType.SEDAN);
    model.setDrive(DriveType.RWD);
    model.setPrice(new Price(new BigDecimal("3000000")));
    carModelRepository.save(model);

    List<UUID> compatibleIds = List.of(model.getId());

    wheelPart = new Part(UUID.randomUUID(), PartType.WHEEL, new BigDecimal("50000"), compatibleIds);
    transmissionPart =
        new TransmissionPart(
            UUID.randomUUID(),
            PartType.TRANSMISSION,
            new BigDecimal("100000"),
            compatibleIds,
            TransmissionType.AUTOMATIC);
    steeringWheelPart =
        new Part(
            UUID.randomUUID(), PartType.STEERING_WHEEL, new BigDecimal("20000"), compatibleIds);
    interiorPart =
        new Part(UUID.randomUUID(), PartType.INTERIOR, new BigDecimal("150000"), compatibleIds);
    colorPart =
        new ColorPart(
            UUID.randomUUID(), PartType.COLOR, new BigDecimal("0"), compatibleIds, "Black");

    partRepository.save(wheelPart);
    partRepository.save(transmissionPart);
    partRepository.save(steeringWheelPart);
    partRepository.save(interiorPart);
    partRepository.save(colorPart);
  }

  @Test
  void testCreateStockOrder() {
    CarConfiguration config =
        new CarConfiguration.Builder(model)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();

    Car car = new Car(UUID.randomUUID(), config);
    StockOrder order = orderService.createStockOrder(client, car);

    UUID orderId = order.getId();
    assertNotNull(orderService.findOrderById(orderId));

    String state = order.getState().getName(order);

    assertEquals("CREATED_STOCK", state);
  }

  @Test
  void testOrderLifecycle_NextStep() {
    CarConfiguration config =
        new CarConfiguration.Builder(model)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();

    Car car = new Car(UUID.randomUUID(), config);
    StockOrder order = orderService.createStockOrder(client, car);

    UUID orderId = order.getId();
    orderService.findOrderById(orderId);

    assertEquals("CREATED_STOCK", order.getState().getName(order));

    order.next();
    assertEquals("APPROVED_BY_MANAGER", order.getState().getName(order));

    order.next();
    assertEquals("AWAITING_PAYMENT", order.getState().getName(order));

    order.next();
    assertEquals("PAID", order.getState().getName(order));

    order.next();
    assertEquals("READY_FOR_ISSUE", order.getState().getName(order));

    order.next();
    assertEquals("COMPLETED", order.getState().getName(order));

    assertThrows(IllegalStateException.class, order::cancel);
  }

  @Test
  void testCreateOrderNoManagerException() {
    userRepository.delete("petr@gmail.com");

    CarConfiguration config =
        new CarConfiguration.Builder(model)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();

    Car car = new Car(UUID.randomUUID(), config);

    assertThrows(IllegalStateException.class, () -> orderService.createStockOrder(client, car));
  }

  @Test
  void testCustomOrderLifecycle() {
    CarConfiguration config =
        new CarConfiguration.Builder(model)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();

    CustomOrder order = orderService.createCustomOrder(client, config);

    assertEquals("CREATED_CUSTOM", order.getState().getName(order));

    order.next();
    order.next();
    order.next();
    order.next();

    assertEquals("AWAITING_DELIVERY", order.getState().getName(order));
  }

  @Test
  void testCancelOrder() {
    CarConfiguration config =
        new CarConfiguration.Builder(model)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();

    Car car = new Car(UUID.randomUUID(), config);
    StockOrder order = orderService.createStockOrder(client, car);

    orderService.cancelOrder(order.getId());
    assertEquals("CANCELLED", order.getState().getName(order));
  }
}
