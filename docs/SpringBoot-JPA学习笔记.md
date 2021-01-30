1. 添加依赖
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    ```
2. 添加数据源及JPA配置
    ```properties
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.url=jdbc:mysql://127.0.0.1:3306/springboot_study6?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT
    spring.datasource.username=root
    spring.datasource.password=root
    # config hibernate
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```
3. 编写Entity实体类
    ```java
    @Entity@Data@NoArgsConstructor
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id ;
        // 用户名称
        @Column
        private String name ;
        // 创建时间
        @Column(name = "create_time")
        private Date createTime ;  
        // 部门id
        @ManyToOne
        @JoinColumn(name = "department_id")
        private Department department ;
    }
    @Entity@Data@NoArgsConstructor
    public class Department {
         @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
         private Integer id ;
         @Column
         private String name ;
         @Column
         @OneToMany(mappedBy = "department")
         private Set<User> users = new HashSet<>() ;
     }
    ```
4. 编写Repository接口
    ```java
    public interface UserRepository extends JpaRepository<User, Integer> {
        List<User> findByName(String name) ;
    }
    ```
5. 编写单元测试
    ```java
    @Slf4j
    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = JpaBasicApiApplication.class)
    public class UserServiceTest {
        @Autowired
        private UserService userService ;
        @Test
        public void addUser(){
            User user = new User() ;
            user.setName("yicj");
            user.setCreateTime(new Date());
            userService.addUser(user) ;
        }
        @Test
        public void findAll(){
            Sort sort = Sort.by("id") ;
            List<User> users = userService.findAll(sort);
            log.info("users : {}", users);
        }
        @Test
        public void getAllUser(){
            int page = 1 ;
            int size = 10;
            List<User> allUser = userService.getAllUser(page, size);
            log.info("all users : {}", allUser);
        }
        @Test
        public void findByName(){
            String name = "yicj" ;
            List<User> list = userService.findByName(name);
            log.info("list : {}", list);
        }
    }
    ```