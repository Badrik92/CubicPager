package com.badrik.cubicpager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badrik.cubicpager.ui.theme.CubicPagerTheme
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CubicPagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HorizontalPagerShow()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun HorizontalPagerShow() {
    val pictures = listOf(
        R.drawable.dubai,
        R.drawable.gorod_kuala_lumpur,
        R.drawable.hongkong,
        R.drawable.newyork,
        R.drawable.paris,
        R.drawable.singapur,
        R.drawable.vankuver,
        R.drawable.dubai,
        R.drawable.gorod_kuala_lumpur,
        R.drawable.hongkong,
        R.drawable.newyork,
        R.drawable.paris,
        R.drawable.singapur,
        R.drawable.vankuver
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val horizontalPagerState = rememberPagerState(pageCount = {
            pictures.count()
        })
        val verticalPagerState = rememberPagerState(pageCount = {
            pictures.count()
        })
        HorizontalPager(
            state = horizontalPagerState,
            modifier = Modifier.size(250.dp)
        ) { page ->
            Box(
                modifier = Modifier
                    // Определяем размер окна
                    .size(250.dp)
                    .applyCubic(horizontalPagerState, page)
            ) {
                Image(
                    painter = painterResource(id = pictures[page]),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                )
            }

        }
        VerticalPager(
            state = verticalPagerState,
            modifier = Modifier.size(250.dp)
        ) { page ->
            Box(
                modifier = Modifier
                    // Определяем размер окна
                    .size(250.dp)
                    .applyCubic(verticalPagerState, page, horizontal = false)
            ) {
                Image(
                    painter = painterResource(id = pictures[page]),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                )
            }

        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
        /*
        Этот код определяет расширение модификатора Modifier с именем applyCubic.
        applyCubic принимает 4 аргумента:
        state: Это экземпляр класса PagerState, который предоставляет информацию о состоянии пагинатора, такой как текущая страница и скорость прокрутки.
        page: Это номер страницы, для которой мы хотим применить эффект.
        offsetX: Это смещение в горизонтальном направлении для элемента. Оно используется для определения, когда анимация должна начаться или прекратиться.
        offsetY: Это смещение в вертикальном направлении для элемента. Оно используется для определения, когда анимация должна начаться или прекратиться.
        Эффект применяется с помощью анимации AnimatedVisibility. Внутри анимации происходит смена размера элемента с использованием интерполяции.
        Значение 1f означает, что размер элемента составляет 100% от его исходного размера.
        Значение 0f означает, что размер элемента составляет 0% от его исходного размера.
        Анимация применяется только к элементам, которые находятся на текущей странице пагинатора (state.currentPage == page).
        Если вызов функции applyCubic осуществляется без передачи значений для offsetX и offsetY, анимация будет применяться ко всем элементам.
         */
fun Modifier.applyCubic(state: PagerState, page: Int, horizontal: Boolean = true): Modifier {
    // graphicsLayer - позволяет применять к элементу 2D или 3D преобразования
    return graphicsLayer {
        // Вычисляем смещение для указанной страницы относительно левого края окна просмотра
        // ( анимируем переход между страницами )
        val pageOffset = state.offsetForPage(page)
        val offsetScreenRight = pageOffset < 0f
        // Устанавливаем значение градуса, на который будет повернут элемент
        val deg = if(horizontal) {
            105f
        } else {
            -100f
        }
        // Преобразуем абсолютное значение смещения страницы ( pageOffset.absoluteValue )
        val interpolated = FastOutLinearInEasing.transform(pageOffset.absoluteValue)
        // Вычисляем занчение вращения вокруг оси Y
        val rotation = (interpolated * if (offsetScreenRight) deg else -deg).coerceAtMost(90f)
        if (horizontal) {
            rotationY = rotation
        } else {
            rotationX = rotation
        }
        // Точка опоры для трансформации
        transformOrigin = if (horizontal) {
            TransformOrigin(
                // Определяет положение центра вращения относительно ширины элемента
                pivotFractionX = if (offsetScreenRight) 0f else 1f,
                // Определяет положение центра вращения относительно высоты элемента
                pivotFractionY =  .5f
            )
        } else {
            TransformOrigin(
                pivotFractionY = if (offsetScreenRight) 0f else 1f,
                pivotFractionX = 0.5f
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
        /*PagerState - это основной компонент ViewPager, который содержит информацию о текущем сосотоянии и конфигурации
        Определяем ф-ю offsetForPage. Внутри ф-ии происходит вычисление значения смещения
        для заданной страницы page. Значение смещения будет использова ться для скроллинга к странице
        offsetForPage вернет значение смещния для страницы page, что позволит выполнить скроллинг к этой странице
         */
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

@OptIn(ExperimentalFoundationApi::class)
        /*Ф-я принимает параметр (page) и возвращает занчение смещения начала это страницы
        Оффсет определяет расстояние между началом ViewPager и началом видимой области (страницы)
        Если смещение отрицательное, это означает, что часть предыдущей страницы видна на экране.
        Если смещение положительное, это означает, что часть следующей страницы видна на экране.
        После того, как получено смещение начала для страницы, функция startOffsetForPage() применяет
        функцию coerceAtLeast(0f) для ограничения значения смещения начала страницы значением 0 или больше.
        Это делается для того, чтобы гарантировать, что значение смещения будет не отрицательным.
         */
fun PagerState.startOffsetForPage(page: Int) = offsetForPage(page).coerceAtLeast(0f)

/* endOffsetForPage - возвращает конечный отступ для заданной страницы
Функция endOffsetForPage работает следующим образом:
2.1. Она получает отступ для заданной страницы с помощью функции offsetForPage.
2.2. Затем, она преобразует отступ в конечный отступ, который не превышает значение 0.
Это делается с помощью функции coerceAtMost.
2.3. Наконец, функция возвращает конечный отступ.
Таким образом, endOffsetForPage возвращает конечный отступ для заданной страницы.
Этот отступ может быть использован для различных целей, например, для определения того,
следует ли показывать прокрутку на страницу или нет.
 */
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.endOffsetForPage(page: Int) = offsetForPage(page).coerceAtMost(0f)



