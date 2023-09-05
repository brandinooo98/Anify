const PlaylistEmbed = (props) => {
  const apiResults = props.apiResults.playlistEmbed;
  return (
    <div>
      {apiResults && (
        <div
          className="center"
          dangerouslySetInnerHTML={{ __html: apiResults }}
        />
      )}
    </div>
  );
};
export default PlaylistEmbed;
