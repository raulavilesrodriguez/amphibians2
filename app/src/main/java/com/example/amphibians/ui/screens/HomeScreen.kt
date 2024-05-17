package com.example.amphibians.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.amphibians.R
import com.example.amphibians.model.AmphibiansPhoto
import com.example.amphibians.ui.theme.AmphibiansTheme

@Composable
fun HomeScreen(
    amphibiansUiState: AmphibiansUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    when(amphibiansUiState){
        is AmphibiansUiState.Loading -> LoadingScreen(modifier = modifier.size(dimensionResource(id = R.dimen.loading_screen)))
        is AmphibiansUiState.Success ->
            PhotosScreen(
                amphibiansUiState.amphibians,
                modifier = modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.padding_medium),
                        top = dimensionResource(id = R.dimen.padding_medium),
                        end = dimensionResource(id = R.dimen.padding_medium)
                    ),
                contentPadding = contentPadding
            )
        else -> ErrorScreen(retryAction, modifier)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier){
    Image(
        modifier = modifier.fillMaxSize(),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(
            text = stringResource(id = R.string.loading_failed),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun AmphibiansCard(
    amphibian: AmphibiansPhoto,
    modifier: Modifier = Modifier
){
  Card(
      modifier = modifier,
      elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.padding_small))
  ){
      Column(
          horizontalAlignment = Alignment.CenterHorizontally,
      ) {
          Text(
              text = stringResource(R.string.amphibian_title, amphibian.name, amphibian.type),
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Start,
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(dimensionResource(R.dimen.padding_medium)),
          )
          AsyncImage(
              model = ImageRequest.Builder(context = LocalContext.current)
                  .data(amphibian.imgSrc)
                  .crossfade(true)
                  .build(),
              error = painterResource(R.drawable.ic_broken_image),
              placeholder = painterResource(R.drawable.loading_img),
              contentDescription = stringResource(id = R.string.amphibians_item),
              contentScale = ContentScale.FillWidth,
              modifier = Modifier
                  .fillMaxWidth()
          )
          Text(
              text = amphibian.description,
              style = MaterialTheme.typography.bodyMedium,
              textAlign = TextAlign.Justify,
              modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
          )
      }
  }  
}

@Composable
private fun PhotosScreen(
    photos: List<AmphibiansPhoto>,
    modifier: Modifier =Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_vertical)),
        modifier = modifier,
    ) {
        items(items = photos, key = {photo -> photo.name}){
                photo -> AmphibiansCard(
                        amphibian = photo,
                        modifier = modifier.fillMaxSize()
                        )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmphibiansCardPreview(){
    AmphibiansTheme {
        AmphibiansCard(
            AmphibiansPhoto(
                stringResource(id = R.string.name_test),
                stringResource(id = R.string.type_test),
                stringResource(id = R.string.description_test),
                stringResource(id = R.string.img_src)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PhotosScreenPreview(){
    AmphibiansTheme {
        val amphibiansData = List(6) {
            AmphibiansPhoto("Sapo - $it","$it", "Frog good", "")
        }
        PhotosScreen(amphibiansData,  Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview(){
    AmphibiansTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview(){
    AmphibiansTheme {
        ErrorScreen({})
    }
}
